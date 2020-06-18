from flask import Flask
from flask_restful import Api, Resource, reqparse

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
            return bill, 200
        return "No bill found", 404


api.add_resource(Bill, "/bill/<string:bill_id>")

app.run(debug=True)