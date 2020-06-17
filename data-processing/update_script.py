import requests
from datetime import date, timedelta
from typing import List


def main():
    # get update since 12 hours ago
    # time = (datetime.datetime.now() - timedelta(hours=12)).replace(microsecond=0).isoformat()
    time = "2020-06-15T00:00:00"

    govInfoAPIKey = "oEA3hx5DO6bju4YuvyDP4H9eTqbn1T9G8nlkhur6"
    collectionURL = "https://api.govinfo.gov/collections/BILLS/%sZ?offset=0&pageSize=100&congress=116&api_key=%s" % (time, govInfoAPIKey)
    collection = requests.get(collectionURL).json()
    packages = collection['packages']
    ids = set(map(lambda x: x['packageId'], packages))
    data = list(map(lambda x: getTxt(x), ids))
def getTxt(package_id: str) -> (str, List[str], str):
    # get package summary
    govInfoAPIKey = "oEA3hx5DO6bju4YuvyDP4H9eTqbn1T9G8nlkhur6"
    summaryURL = "https://api.govinfo.gov/packages/%s/summary?api_key=%s" % (package_id, govInfoAPIKey)
    summary = requests.get(summaryURL).json()
    txtURL = summary['download']['txtLink'] + "?api_key=" + govInfoAPIKey
    txt = requests.get(txtURL).text 
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


if __name__ == "__main__":
    main()
    