# GitLaw ⚖️
### Version Control for Legislations
### :pen:: Leon Liang
----

## How to contribute
 - open a pull request
 - get access to source code
 	- Prerequisite
 		1. install javajdk8 and Scala **2.11** (will run into compatibility issues with 2.12)
 		2. install Spark, Cassandra on local machine/remote servers
	 1. `git clone` this repo
 	 2. make your contribution
	 3. ?
	 4. profit
 
## Introduction
 - Version Control for Laws and Legislations that [helps making the creation and passage of legislation a more transparant process.](https://blog.abevoelker.com/gitlaw-github-for-laws-and-legal-documents-a-tourniquet-for-american-liberty/)
 
## Architecture

 ![Data pipeline](https://github.com/leonliangsc/GitLaw/blob/master/images/Data%20Pipeline%20(1).png)

## Dataset

➡️Data sources: 
 - file format: xml
 - [Library of Congress](https://www.congress.gov/advanced-search/legislation) and its [data dump](https://github.com/usgpo/bulk-data)
 - [Congressional Record](https://www.congress.gov/congressional-record)

🔄Processing: 
 - storing LOC data dump in AWS S3, processing in batch using Spark
 - getting files into S3 using [Congress web scraper](https://github.com/unitedstates/congress):
  	1. retrieving packages from collections of Bills
	  ```json
	  {
		"count": 197925,
		"message": null,
		"nextPage": "https://api.govinfo.gov/collections/BILLS/2018-01-01T00:00:00Z/?offset=100&pageSize=100",
		"previousPage": null,
		"packages": [{
				"packageId": "BILLS-115hr4403rh",
				"lastModified": "2018-04-17T06:51:38Z",
				"packageLink": "https://api.govinfo.gov/packages/BILLS-115hr4403rh/summary"
			}, {
				"packageId": "BILLS-115hr5503ih",
				"lastModified": "2018-04-14T04:00:56Z",
				"packageLink": "https://api.govinfo.gov/packages/BILLS-115hr5503ih/summary"
			}, ...
		]
	}
	  ```
 - run the following commmand to start spark batch processing data from S3 -> Cassandra
	```bash
	spark-submit --master spark://master_ip:7007 --packages com.datastax.spark:spark-cassandra-connector_2.11:2.5.0,com.amazonaws:aws-java-sdk:1.7.4,org.apache.hadoop:hadoop-aws:2.7.7 --conf spark.hadoop.fs.s3a.endpoint=s3.us-east-2.amazonaws.com --conf spark.executor.extraJavaOptions=-Dcom.amazonaws.services.s3.enableV4=true --conf spark.driver.extraJavaOptions=-Dcom.amazonaws.services.s3.enableV4=true --conf spark.cassandra.connection.host=<cassandra_server_ip> --conf spark.sql.extensions=com.datastax.spark.connector.CassandraSparkExtensions ~/GitLaw/data-processing/processor/target/scala-2.11/processor_2.11-0.1.jar
	```
 

⬅️Output: 
 - real time U.S. Laws and Legislation in Cassandra
 - public facing API
 - web hook
 - CLI tool mimicking `git log/blame/revert` 



## :gear:Engineering Challenge: 
1. [Tracking evolution of data at large scale](https://sites.google.com/insightdatascience.com/de-la-fellow-hub-2020b/pre-session/project-prep/project-seeds#h.p_bFdKFDhnY8FI)
2. Change-date-capture (CDC)
3. Database Schema design

## :robot:Trade-offs:
 - todo

