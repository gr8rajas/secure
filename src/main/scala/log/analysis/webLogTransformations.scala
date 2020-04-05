package log.analysis

import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.sql
import org.apache.spark.sql.expressions.Window

class webLogTransformations extends Serializable with StrictLogging {


  /** This method parses Web logs,cleanses and returns standard DataFrame with required Schema */

  def readAndParse(logData:sql.DataFrame): sql.DataFrame = {

    logger.info("Parsing the logs")
    import org.apache.spark.sql.functions._
    val parsedData =
      logData.select(
        trim(regexp_extract(col("value"), """([^\s]+\s)""", 1)).alias("host"),
        regexp_extract(col("value"), """.*\[(\d\d/\w{3}/\d{4})""", 1).alias("receive_date"),
        trim(regexp_extract(col("value"), """.*"\w+\s+([^\s]+)\s+HTTP.*"""", 1)).alias("url"),
        regexp_extract(col("value"), """.*"\s+([^\s]+)""", 1).alias("status"),
        regexp_extract(col("value"), """.*\s+(\d+)$""", 1).alias("content_size")
      )

    val filteredNull = parsedData.filter(" host IS NOT NULL ").where("url != '/'")
    val filteredData = filteredNull.where("url != ''").where("host != ''")

    filteredData
  }


  /** This method first groups the data and get the host count(number of visited).
    * then applying window analytic function to get the highest count for each group of host and receive date,
    * Now applying one more analytic function to get the top N visitors
    * */

  def transformData(filteredData:sql.DataFrame,topFrequentVisitor:Int): sql.DataFrame = {

    logger.info("transforming the logs")

    import org.apache.spark.sql.functions._
    val  groupedData  = filteredData.groupBy("receive_date","host","url")
      .agg(count("host").as("no_of_times_visited"))

    val windowSpecForHost = Window.partitionBy("receive_date","host").orderBy(desc("no_of_times_visited"))
    val rowNumSpec = row_number().over(windowSpecForHost)
    val rowNumOutput = groupedData.withColumn("rnk",rowNumSpec).where("rnk=1").drop("rnk")

    val windowSpecTop = Window.partitionBy("receive_date").orderBy(desc("no_of_times_visited"))
    val rowNumSpecTop = row_number().over(windowSpecTop)
    val groupedOutput = rowNumOutput.withColumn("top",rowNumSpecTop).where(s"top <=$topFrequentVisitor")

//    val output = groupedOutput.withColumnRenamed("dr","top").orderBy(col("receive_date"),desc("host_count"))
    val output = groupedOutput.orderBy(col("receive_date"),desc("no_of_times_visited"))

    output
  }

}



/** This Window Analytic function returns all visitors with highest  count in descending order */
//    val windowSpec = Window.partitionBy("receive_date").orderBy(desc("no_of_times_visited"))
//    val rankSpec = dense_rank().over(windowSpec)
//    val denseRankOutput = groupedData.withColumn("dr",rankSpec)


///** This method returns distinct dates inorder to get frequent visitors for each day */
//
//def distinctDates(filteredData:sql.DataFrame) : Int ={
//
//  val distinctDateCount = filteredData.select("receive_date").distinct().count()
//  distinctDateCount.toInt
//}