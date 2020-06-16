package Processor

import spark.implicits._
import org.apache.spark.sql.types._
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.fs.s3a._
import scala.collection.JavaConverters._
import java.net.URI


object Processor extends App{
  // url parser
  val baseURL = "s3a://gitlawbucket"
//  val congress = Seq("116", "115", "114", "113")
//  val docuType = Seq("amendments", "bills")
//  val house = Seq("hconres", "hjres", "hr", "hres", "s", "sconres", "sjres", "sres")
//  val textVersion = "text-versions"
//

  // start spark session
  val spark = SparkSession
    .builder()
    .appName("Processor")
    .getOrCreate()


  // DFS get all metadata file and text files
  val sc = spark.sparkContext
  val hadoopConf = sc.hadoopConfiguration
  val exampleTxt = "s3a://gitlawbucket/116/bills/hr/hr1/text-versions/eh/document.txt"
  val path = "s3a://gitlawbucket/116/bills/hr/hr1/data.json"
  def getFiles(dir: String, df: org.apache.spark.sql.DataFrame): Unit = {
    // get all files / folders in the directory
    val contents = FileSystem.get(new URI(dir), hadoopConf).listStatus(new Path(dir)).map {
      _.getPath.toString
    }

//    // if document.txt, read txt and join with dataframe
//    if (contents.contains(dir)) {
//      assert(contents.length == 1)
//      if (contents(0).contains("document.txt") && df != null) {
//        println("found document.txt")
//        val text = readText(dir)
//        // TODO: join text into df
//
//      }
//    } else {
      // if data.json, read json and pass in dfs
    contents.foreach(d => if (d.matches(".*/data.json")) {
      val bill_info = readDataJson(d)
      contents.foreach(txt_ver => if (txt_ver.matches(".*/text-versions")) {
        println("Enter path: " + txt_ver)
        getText(txt_ver, bill_info)
      })
    } else {
      // continue DFS
      getFiles(d, null)
    })
//    }
  }

  def getText(dir: String, df: org.apache.spark.sql.DataFrame): Unit = {
    val contents = FileSystem.get(new URI(dir), hadoopConf).listStatus(new Path(dir)).map {
      _.getPath.toString
    }
    // schema:
    // version | text
    // --------|------
    // ih      | The new bill...
//    val schemaString = "version text"
//    val fields = schemaString.split(" ").map(fn => StructField(fn, StringType, nullable = true))
//    val schema = StructType(fields)
//
//    // get all text files
//    val txtRdd = contents.map(p => p + "/document.txt").map(p => sc.textFile(p))
//    val sufRDD = contents.map(f => f.split("/").last).map(str => sc.parallelize(List(str)))
//    val zipped = sufRDD zip txtRdd // :org.apache.spark.rdd.RDD[Array[(String, String)]]
//    val rowRDD = zipped.map {
//      case (x, y) => Row(x, y)
//    } // :org.apache.spark.rdd.RDD[org.apache.spark.sql.Row]
    val txtArr = contents.map(p => p + "/document.txt").map(p => readText(p))
    var temp = df
    for ( i <- 0 to txtArr.length - 1) {
      temp = temp.withColumn("version" + i, lit(txtArr(i)))
    }
     temp.write.format("org.apache.spark.sql.cassandra").options(Map("keyspace"->"gitlaw","table"->"bills")).save()
  }

  def readDataJson(path: String): org.apache.spark.sql.DataFrame = {
    spark.read.option("multiLine", true).option("mode", "PERMISSIVE").json(path)
      .select("bill_id", "sponsor", "official_title")
      .withColumnRenamed("sponsor", "author")
      .withColumn("versions", lit(Array.empty[String]))


    //    val dir = sc.wholeTextFiles("s3a://gitlawbucket/116/bills/sconres/sconres1/")

    // collect info about directory
//    val filename: Array[String] = dir.collect().map(f => f._1)

    // query the json file using spark sql
//    df.createOrReplaceTempView("data")

    // read from Cassandra
//    val conf = new SparkConf(true).set("spark.cassandra.connection.host", "172.31.47.68")
//    val sqlContext = spark.sqlContext
//    val df = sqlContext.read.format("org.apache.spark.sql.cassandra").options(Map("table" -> "bills", "keyspace" -> "gitlaw")).load()
    // retrieve
//    spark.sql("SELECT bill_id, sponsor AS author, official_title FROM data").withColumn("versions", lit(Array.empty[String]))
//      df.select("bill_id", "sponsor", "official_title").withColumnRenamed("sponsor", "author").withColumn("versions", lit(Array.empty[String]))
  }

  // read .txt file
  def readText(path: String) : String = {
    spark.read.textFile(path).collect().mkString("\n")
  }
}
