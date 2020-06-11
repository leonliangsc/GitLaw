package Processor

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext
import scala.collection.JavaConverters._
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ListObjectsV2Request
import com.datastax.spark.connector._


object Processor extends App{
  // url parser
  val baseURL = "s3a://gitlawbucket"
  val congress = Seq("116", "115", "114", "113")
  val docuType = Seq("amendments", "bills")
  val house = Seq("hconres", "hjres", "hr", "hres", "s", "sconres", "sjres", "sres")
  val textVersion = "text-versions"


  // start spark session
  val spark = SparkSession
    .builder()
    .appName("Processor")
    .getOrCreate()

  // read in meta data from data.json
  val df = spark.read.option("multiLine", true).option("mode", "PERMISSIVE").json("s3a://gitlawbucket/116/bills/sconres/sconres1/data.json")
  val dir = sc.wholeTextFiles("s3a://gitlawbucket/116/bills/sconres/sconres1/")

  // collect info about directory
  val filename: Array[String] = dir.collect().map(f => f._1)

  // query the json file using spark sql
  df.createOrReplaceTempView("data")
  val id = spark.sql("SELECT bill_id FROM data")
  val actions = spark.sql("SELECT actions FROM data")

  // read from Cassandra
  val conf = new SparkConf(true).set("spark.cassandra.connection.host", "172.31.47.68")
  val sqlContext = spark.sqlContext
  val df = sqlContext.read.format("org.apache.spark.sql.cassandra").options(Map("table" -> "bills", "keyspace" -> "gitlaw")).load()

  // write to Cassandra
  val store = spark.sql("SELECT bill_id, sponsor AS author, official_title FROM data")
  // TODO: join text
  store.write.format("org.apache.spark.sql.cassandra").options(Map("keyspace"->"gitlaw","table"->"bills")).save()
}
