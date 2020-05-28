# WikiVersionControl
### Git Version Control, but for Wikipedia.
----
➡️Input: 
 - WikiAPI/Web scraping/Web archive

🔄Processing: 
 - processing Wikipedia data dump and storing in Spark
 - querying entries as key-sorted-key-value pairs in NoSQL database Cassandra. 
 - processing real time changes using Kafka


⬅️Output: 
 - real time Wikipedia in Cassandra
 - CLI tool mimicking `git log/blame/revert` 

Use case:
1. Revert malicious Wiki entry vandalism
2. Look up/ compare previous entry definition

Engineering Challenge: 
1. Change-date-capture (CDC)
2. Database Schema design


