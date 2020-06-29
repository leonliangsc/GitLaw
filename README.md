# [GitLaw](https://gitlaw.info) ⚖️
### Version Control for Legislations
### :pen:: Leon Liang
----

## How to contribute
 1. open an issue
 2. get access to source code
 	- Prerequisite
 		1. install Java8 and Scala **2.11** (will run into compatibility issues with 2.12)
 		2. install Spark, Cassandra on local machine/remote servers
		3. install sbt for dependency management and packaging/shipping
	 1. `git clone` this repo
 	 2. make your contribution
	 3. open a pull request
	 4. ?
	 5. profit
	 
----
 
## Introduction
 - Version Control for Laws and Legislations that [helps making the creation and passage of legislation a more transparant process.](https://blog.abevoelker.com/gitlaw-github-for-laws-and-legal-documents-a-tourniquet-for-american-liberty/)
 
------
## Architecture

 ![Data pipeline](https://github.com/leonliangsc/GitLaw/blob/master/images/Data%20Pipeline%20(1).png)

----
## Dataset

➡️Data sources: 
 - [Library of Congress](https://www.congress.gov/advanced-search/legislation) and its [data dump](https://github.com/usgpo/bulk-data)
 - [Congressional Record](https://www.congress.gov/congressional-record)
 - [follow the steps to scrap data](https://github.com/leonliangsc/GitLaw/tree/master/ingestion)

🔄Processing: 
 - getting files into S3 using [Congress web scraper](https://github.com/unitedstates/congress):
 - [follow the steps](https://github.com/leonliangsc/GitLaw/tree/master/data-processing) to start spark batch processing data from S3 -> Cassandra and [create database schema](https://github.com/leonliangsc/GitLaw/tree/master/database-scripts)
 

⬅️Output: 
 - up-to-date U.S. Laws and Legislation in Cassandra
 - public facing API
 - A [minimal web UI](https://gitlaw.info)
----

## :gear:Engineering Challenge: 
1. Cleaning, aggregating data from various formats
2. Optimizing Spark performance with customized partitioner
3. Integrating diff-ing algorithm
----
## :robot:Trade-offs:
 - NoSQL vs SQL
 	1. Due to the less-oftenly-changed nature of law schema, GitLaw can live with just one table, thus eliminating join operation and providing better performance
	2. When working with just one table, I value the speed of retrieval and ease of scalability.
	3. Overall, the ease of use is the main concern when building a 4 week MVP.
 - Spark RDD vs Dataframe
 	1. Dataframe being a table-structured data object, would have been, in the hindsight, a better choice for working with JSON formatted file
	2. However, as far as I researched, operations on RDD are well-documented, thanks to Datastax.
	3. The main concern is still the ease of development, RDD despite being a less suitable data object for my use case, allowed me to shorted dev time with its documentation.

