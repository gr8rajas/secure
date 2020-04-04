package log.analysis

import java.io.Serializable

import org.apache.spark.SparkFiles
import org.apache.spark.sql.SparkSession

object webLogAnalysis extends Serializable {

  def main(args: Array[String]): Unit = {

    System.out.println("\n Number of top visitors are you looking for? Default gives top 3 else Please enter \n")

    val readInput = scala.io.StdIn.readInt()
    val topFrequentVisitor = if (readInput.toString.isEmpty) 5 else readInput

    System.out.println(s"\n Retrieving results for top $topFrequentVisitor \n")

    val spark = SparkSession.builder.master("local[4]").appName("AnalyzeLogs").getOrCreate()
      spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      spark.sparkContext.setLogLevel("ERROR")

    val dataSource = "ftp://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz"
      spark.sparkContext.addFile(dataSource)
    val file = SparkFiles.get(dataSource.split("/").last)

    System.out.println("Getting the logs \n")
    val readLogData = spark.read.text(file)

    System.out.println("Parsing and Cleansing the logs \n")
    val webLog = new webLogTransformations
    val logData = webLog.readAndParse(readLogData)
    val allDates = webLog.distinctDates(logData)

    System.out.println("Executing the query \n")
    val output  = webLog.transformData(logData,topFrequentVisitor)

    System.out.println("Retrieving Output \n")
    output.show(allDates * topFrequentVisitor,false)
    spark.stop()
  }
}

//    val topFrequentVisitor = if (args.length == 0) 3 else args(0).toInt