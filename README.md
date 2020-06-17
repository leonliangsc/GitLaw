# GitLaw ⚖️
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
 - web hook
 - CLI tool mimicking `git log/blame/revert` 

----

## :gear:Engineering Challenge: 
1. [Tracking evolution of data at large scale](https://sites.google.com/insightdatascience.com/de-la-fellow-hub-2020b/pre-session/project-prep/project-seeds#h.p_bFdKFDhnY8FI)
2. Change-date-capture (CDC)
3. Database Schema design
----
## :robot:Trade-offs:
 - todo

