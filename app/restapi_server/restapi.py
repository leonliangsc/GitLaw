from flask import Flask
from flask_restful import Api, Resource, reqparse
import json

from cassandra.cluster import Cluster 

app = Flask(__name__)
api = Api(app)

class Bill(Resource):
    # simple getter
    def get(self, bill_id: str):
        # start a query session on local Cassandra cluster
        cluster = Cluster()
        session = cluster.connect('gitlaw')

        bill = session.execute('SELECT * FROM bills WHERE bill_id=\'%s\'' % (bill_id))
        if bill.one():
            # parse into dict()
            parsed = {"bill_id": bill.one()[0], "sponsor": bill.one()[1], "official_title": bill.one()[2]}
            for i, text in enumerate(bill.one()[3:]):
                if text:
                    parsed["version%d" % i] = text
            return parsed, 200
        return "Ok", 200


api.add_resource(Bill, "/bill/<string:bill_id>")

# e.g. 127.0.0.1:5000/bill/s1130-116

app.run(debug=True)