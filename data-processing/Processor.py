from pyspark.sql import SparkSession

class Processor:
    def main():
        spark = SparkSession.builder.appName("processor").getOrCreate()

        baseURL = "s3a://gitlawbucket"
        congress = [116, 115, 114, 113]
        docuType = ["amendments", "bills"]
        house = ["hconres", "hjres", "hr", "hres", "s", "sconres", "sjres", "sres"]
        textVersion = "text-versions"

        lines = spark.read.option("multiLine", "true").option("mode", "PERMISSIVE").json("s3a://gitlawbucket/116/bills/sconres/sconres1/data.json")
        print(lines.first())
        


    if __name__ == "__main__":
        main()