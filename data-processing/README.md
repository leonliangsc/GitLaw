# Data Processing
----
## How to run Spark
 1. Spark batch processing built, packaged and shipped with sbt, written in Scala.
 2. clone this repo 
 3. start Spark and Cassandra clusters, get their ip addresses
 4. compile, build and ship the code with sbt, generate .jar file
 5. plug in the ip addresses and path to .jar file into following command:
 ```bash
 spark-submit --master spark://<master_ip>:7007 --packages com.datastax.spark:spark-cassandra-connector_2.11:2.5.0,com.amazonaws:aws-java-sdk:1.7.4,org.apache.hadoop:hadoop-aws:2.7.7 --conf spark.hadoop.fs.s3a.endpoint=s3.us-east-2.amazonaws.com --conf spark.executor.extraJavaOptions=-Dcom.amazonaws.services.s3.enableV4=true --conf spark.driver.extraJavaOptions=-Dcom.amazonaws.services.s3.enableV4=true --conf spark.cassandra.connection.host=<cassandra_server_ip> --conf spark.sql.extensions=com.datastax.spark.connector.CassandraSparkExtensions <path_to_jar>
 ```

## How to run update:
 1. `python3 GitLaw/updator/update_script.py`
 2. or use Cron to schedule daily update:
  - start a Cronjob by typing the following command `crontab -e`
  - append the following command:
  ```bash
  # run python bills update script every 12 hours
  0 */12 * * * python3 ~/GitLaw/data-processing/updator/update_script.py
  ```
