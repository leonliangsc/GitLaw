import requests
from datetime import date, datetime, timedelta
from typing import List

from cassandra.cluster import Cluster 

def main():
    # get update since 12 hours ago
    time = (datetime.now() - timedelta(hours=12)).replace(microsecond=0).isoformat()

    govInfoAPIKey = "oEA3hx5DO6bju4YuvyDP4H9eTqbn1T9G8nlkhur6"
    collectionURL = "https://api.govinfo.gov/collections/BILLS/%sZ?offset=0&pageSize=100&congress=116&api_key=%s" % (time, govInfoAPIKey)
    collection = requests.get(collectionURL).json()
    packages = collection['packages']
    ids = set(map(lambda x: x['packageId'], packages))
    data = list(map(lambda x: getTxt(x), ids))
    cassWriter(data)

def getTxt(package_id: str) -> (str, List[str], str):
    # get package summary
    govInfoAPIKey = "oEA3hx5DO6bju4YuvyDP4H9eTqbn1T9G8nlkhur6"
    summaryURL = "https://api.govinfo.gov/packages/%s/summary?api_key=%s" % (package_id, govInfoAPIKey)
    summary = requests.get(summaryURL).json()
    txtURL = summary['download']['txtLink'] + "?api_key=" + govInfoAPIKey
    txt = requests.get(txtURL).text.replace('`', '\"').replace('\'', '\"')
    official_title = summary['title']

    # schema:
    # CREATE TABLE gitlaw.bills (
    #     bill_id text PRIMARY KEY,
    #     author frozen<sponsor>,
    #     official_title text,
    #     versions set<text>
    # )
    # CREATE TYPE gitlaw.sponsor (
    #     bioguide_id text,
    #     district text,
    #     name text,
    #     state text,
    #     title text,
    #     type text
    # );
    author = dict()
    if 'members' in summary:
        sponsor = summary['members'][0]
        author = {"bioguide_id": sponsor['bioGuideId'], "district": sponsor['chamber'], "name": sponsor['memberName'], "title": sponsor['role'], "type": sponsor['party']}
    else:
        author = {"bioguide_id": "", "district": "", "name": "", "title": "", "type": ""}
    return (package_id, txt, official_title, author)


def cassWriter(data) -> None:
    # start a query session
    cluster = Cluster()
    session = cluster.connect('gitlaw')

    # print(data[0][0], data[0][1], data[0][2], data[0][3])

    # write all rows to Cassandra
    for (bill_id, txt, official_title, author) in data:
        # check if this bill exist
        exist = session.execute('SELECT * FROM bills WHERE bill_id=\'%s\'' % (bill_id))
        if exist.one() != None:
            print("updating %s" % [bill_id])
            # bill exists, append latest version only
            counter = 0
            version = session.execute("SELECT version%s FROM bills WHERE bill_id=\'%s\'" % (counter, bill_id))
            while version:
                counter += 1
                version = session.execute("SELECT version%s FROM bills WHERE bill_id=\'%s\'" % (counter, bill_id))
            counter += 1
            session.execute("UPDATE bills SET version%s=%s WHERE bill_id=\'%s\'" % (counter, txt, bill_id))
        else:
            print("inserting %s" % [bill_id])
            # insert all
            session.execute("INSERT INTO bills (bill_id, version0, official_title) VALUES (\'%s\', \'%s\', \'%s\')" % (bill_id, txt, official_title))

if __name__ == "__main__":
    main()
    