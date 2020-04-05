package logAnalysis

import java.io.Serializable

import org.apache.spark.SparkFiles
import org.apache.spark.sql.SparkSession

object webLogAnalysis extends Serializable {

  def main(args: Array[String]): Unit = {

    System.out.println("\n Number of top visitors are you looking for? \n Just press enter to give top 3 visitors else enter number \n")

    val readInput = scala.io.StdIn.readLine()
    val topFrequentVisitor = if (readInput =="") 3 else readInput.toInt

    System.out.println(s"\n Retrieving results for top $topFrequentVisitor \n")

    val spark = SparkSession.builder.master("local[4]").appName("AnalyzeLogs").getOrCreate()
      spark.conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      spark.conf.set("spark.sql.shuffle.partitions",10)
      spark.sparkContext.setLogLevel("ERROR")

    val dataSource = "ftp://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz"
      spark.sparkContext.addFile(dataSource)
    val file = SparkFiles.get(dataSource.split("/").last)

    System.out.println("Getting the logs \n")
    val readLogData = spark.read.text(file)

    System.out.println("Parsing and Cleansing the logs \n")
    val webLog = new webLogTransformations
    val logData = webLog.readAndParse(readLogData)

    System.out.println("Executing the query \n")
    val output  = webLog.transformData(logData,topFrequentVisitor)

    System.out.println("Retrieving Output \n")
    output.show(500,false)
    spark.stop()
  }
}
