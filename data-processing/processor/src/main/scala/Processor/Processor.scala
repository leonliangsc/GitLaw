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
import com.datastax.spark.connector._
import scala.collection.JavaConverters._
import java.net.URI


object Processor extends App{
  val baseURL = "s3a://gitlawbucket"

  // config
  val spark = SparkSession
    .builder()
    .appName("Processor")
    .getOrCreate()
  val sc = spark.sparkContext
  val hadoopConf = sc.hadoopConfiguration

  // recursive DFS function to search data.json
  def getFiles(dir: String, df: org.apache.spark.sql.DataFrame): Unit = {
    // get all files / folders in the directory
    println(dir)
    val contents = FileSystem.get(new URI(dir), hadoopConf).listStatus(new Path(dir)).map {
      _.getPath.toString
    }
    contents.foreach(d => if (d.matches(".*/data.json")) {
      val bill_info = readDataJson(d)
      contents.foreach(txt_ver => if (txt_ver.matches(".*/text-versions")) {
        println("Enter path: " + txt_ver)
        getText(txt_ver, bill_info)
      })
    } else {
      // continue DFS
      if (!contents.contains(dir) && !dir.contains("amendments")) {
        getFiles(d, null)
      }
    })
  }

  // takes in the df with metadata and append texts to df
  // write to db
  def getText(dir: String, df: org.apache.spark.sql.DataFrame): Unit = {
    // get all texts
    val txtArr = FileSystem.get(new URI(dir), hadoopConf).listStatus(new Path(dir)).map {
      _.getPath.toString
    }.map(p => p + "/document.txt").map(p => readText(p))

    // append to dataframe
    var temp = df
    for ( i <- 0 to txtArr.length - 1) {
      temp = temp.withColumn("version" + i, lit(txtArr(i)))
    }
     temp.write.format("org.apache.spark.sql.cassandra").options(Map("keyspace"->"gitlaw","table"->"bills")).save()
  }

  // read .json file, returns a dataframe
  def readDataJson(path: String): org.apache.spark.sql.DataFrame = {
    spark.read.option("multiLine", true).option("mode", "PERMISSIVE").json(path)
      .select("bill_id", "sponsor", "official_title")
      .withColumnRenamed("sponsor", "author")
      .withColumn("versions", lit(Array.empty[String]))
  }

  // read .txt file and return the contents as String
  def readText(path: String) : String = {
    spark.read.textFile(path).collect().mkString("\n")
  }

  // start processing
  getFiles(baseURL, null)
}
