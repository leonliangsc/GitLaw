# GitLaw ⚖️
### Version Control for Laws and Legal Documents.
### :pen:: Leon Liang
----

#### Use case:
 - [Help making the creation and passage of legislation a more transparant process.](https://blog.abevoelker.com/gitlaw-github-for-laws-and-legal-documents-a-tourniquet-for-american-liberty/)
 
 
➡️Data sources: 
 - [Library of Congress](https://www.congress.gov/advanced-search/legislation) and its [data dump](https://github.com/usgpo/bulk-data)
 - [Congressional Record](https://www.congress.gov/congressional-record)

🔄Processing: 
 - storing LOC data dump in AWS S3, processing in batch using Spark
 - querying entries as key-sorted-key-value pairs in NoSQL database Cassandra. 
 - processing real time changes using Kafka and pipe into Spark
 
 ![Data pipeline](https://drive.google.com/drive/folders/1gzjnUHGAppNoJebOcm8dRrgcPg_Bz0DE)


⬅️Output: 
 - real time U.S. Laws and Legislation in Cassandra
 - public facing API
 - web hook
 - CLI tool mimicking `git log/blame/revert` 



:gear:Engineering Challenge: 
1. [Tracking evolution of data at large scale](https://sites.google.com/insightdatascience.com/de-la-fellow-hub-2020b/pre-session/project-prep/project-seeds#h.p_bFdKFDhnY8FI)
2. Change-date-capture (CDC)
3. Database Schema design


