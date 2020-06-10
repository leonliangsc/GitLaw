import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SQLContext
import scala.collection.JavaConverters._
// import com.amazonaws.services.s3.AmazonS3Client
// import com.amazonaws.services.s3.model.ListObjectsV2Request


object Processor {
    val baseURL = "s3a://gitlawbucket"
    val congress = List(116, 115, 114, 113)
    val docuType = List("amendments", "bills")
    val house = List("hconres", "hjres", "hr", "hres", "s", "sconres", "sjres", "sres")
    val textVersion = "text-versions"
    
    def main(args: Array[String]): Unit = {
        // create Spark context and SQL context using SparkSession
        val spark = SparkSession
        .builder()
        .appName("Processor")
        .getOrCreate()

        // open files
        val df = spark.read.json("s3a://gitlawbucket/116/bills/sconres/sconres1/data.json")
        // df.show
        df.first()
    }
}
