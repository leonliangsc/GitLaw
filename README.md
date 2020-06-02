# GitLaw ⚖️
### Version Control for Laws and Legal Documents. [Help making the creation and passage of legislation a more transparant process.](https://blog.abevoelker.com/gitlaw-github-for-laws-and-legal-documents-a-tourniquet-for-american-liberty/)
### :pen:: Leon Liang
----

## How to install and get it up runnning
 - todo
 
## Introduction
 - todo
 
## Architecture

 ![Data pipeline](https://github.com/leonliangsc/GitLaw/blob/master/Data%20Pipeline.png)

## Dataset

➡️Data sources: 
 - file format: xml
 - [Library of Congress](https://www.congress.gov/advanced-search/legislation) and its [data dump](https://github.com/usgpo/bulk-data)
 - [Congressional Record](https://www.congress.gov/congressional-record)

🔄Processing: 
 - storing LOC data dump in AWS S3, processing in batch using Spark
  - getting files into S3:
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
 - querying entries as key-sorted-key-value pairs in NoSQL database Cassandra. 
 - processing real time changes using Kafka and pipe into Spark
 

⬅️Output: 
 - real time U.S. Laws and Legislation in Cassandra
 - public facing API
 - web hook
 - CLI tool mimicking `git log/blame/revert` 



:gear:Engineering Challenge: 
1. [Tracking evolution of data at large scale](https://sites.google.com/insightdatascience.com/de-la-fellow-hub-2020b/pre-session/project-prep/project-seeds#h.p_bFdKFDhnY8FI)
2. Change-date-capture (CDC)
3. Database Schema design

:robot:Trade-offs:
 - todo

