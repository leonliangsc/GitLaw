# WikiVersionControl
### Git Version Control, but for Wikipedia.
----
➡️Input: WikiAPI/Web scraping/Web archive

Processing: process real time changes using Kafka, stored as key-sorted-key-value pairs in NoSQL database (Cassandra). Automate 

Output: real time Wikipedia in Cassandra
CLI tool mimicks git log/blame/revert (optional)

Use case:
1. Revert malicious Wiki entry vandalism
2. Look up/ compare previous entry definition

Engineering Challenge: 
1. Change-date-capture (CDC)
2. Database Schema design


