import logAnalysis.webLogTransformations
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.apache.spark.sql
import org.apache.spark.sql.{Row, SparkSession}


class webLogTransformationsTest extends FunSuite with BeforeAndAfterAll {

  val spark : SparkSession = SparkSession.builder.master("local[2]").appName("AnalyzeLogs").getOrCreate()

  import spark.implicits._
  val sampleData : sql.DataFrame = Seq(
    "199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] \"GET /history/apollo/ HTTP/1.0\" 200 6245",
    "unicomp6.unicomp.net - - [01/Jul/1995:00:00:06 -0400] \"GET /images/KSC-logosmall.gif HTTP/1.0\" 200 3985",
    "205.189.154.54 - - [01/Jul/1995:00:01:06 -0400] \"GET /cgi-bin/imagemap/countdown?99,176 HTTP/1.0\" 302 110",
    "port26.annex2.nwlink.com - - [01/Jul/1995:00:01:04 -0400] \"GET /images/construct.gif HTTP/1.0\" 200 1414",
    "www-a1.proxy.aol.com - - [02/Jul/1995:00:01:09 -0400] \"GET /shuttle/countdown/ HTTP/1.0\" 200 3985",
    "www-a1.proxy.aol.com - - [02/Jul/1995:00:01:09 -0400] \"GET /shuttle/countdown/ HTTP/1.0\" ",
    "www-a1.proxy.aol.com - - [02/Jul/1995:00:01:09 -0400] \"GET /shuttle/countUp/ HTTP/1.0\" 200 1"
  ).toDF


  val logAnalysisTest = new webLogTransformations
  val parseLog : sql.DataFrame = logAnalysisTest.readAndParse(sampleData)
  val transformedLog : sql.DataFrame = logAnalysisTest.transformData(parseLog,2)


  override def afterAll: Unit = {
    spark.stop()
  }


  test("readAndParse method should return output count same as input count")  {
    assert(parseLog.count() === 7)
  }


  test("readAndParse method should read a DataFrame and return parsed Data") {
    assert(parseLog.select("host").take(2) === Array(Row("199.72.81.55"),Row("unicomp6.unicomp.net")))
    assert(parseLog.select("receive_date").take(2) === Array(Row("01/Jul/1995"),Row("01/Jul/1995")))
    assert(parseLog.select("url").take(2) === Array(Row("/history/apollo/"),Row("/images/KSC-logosmall.gif")))

  }

  test("transformData method should read a DataFrame and return frequent visitors data") {
    assert(transformedLog.filter("host= 'www-a1.proxy.aol.com'").select("no_of_times_visited").first() === Row(2))
    assert(transformedLog.filter("host= 'www-a1.proxy.aol.com'").count() === 1)

  }


}








//parseLog.show(10)
//transformedLog.show(10)

//  +------------+--------------------+--------------------+----------+---+
//  |receive_date|                host|                 url|host_count| rn|
//  +------------+--------------------+--------------------+----------+---+
//  | 02/Jul/1995|www-a1.proxy.aol.com| /shuttle/countdown/|         2|  1|
//  | 02/Jul/1995|www-a1.proxy.aol.com|   /shuttle/countUp/|         1|  2|
//  | 01/Jul/1995|unicomp6.unicomp.net|/images/KSC-logos...|         1|  1|
//  | 01/Jul/1995|port26.annex2.nwl...|/images/construct...|         1|  1|
//  | 01/Jul/1995|      205.189.154.54|/cgi-bin/imagemap...|         1|  1|
//  | 01/Jul/1995|        199.72.81.55|    /history/apollo/|         1|  1|
//    +------------+--------------------+--------------------+----------+---+



//+--------------------+------------+--------------------+------+------------+
//|                host|receive_date|                 url|status|content_size|
//+--------------------+------------+--------------------+------+------------+
//|        199.72.81.55| 01/Jul/1995|    /history/apollo/|   200|        6245|
//|unicomp6.unicomp.net| 01/Jul/1995|/images/KSC-logos...|   200|        3985|
//|      205.189.154.54| 01/Jul/1995|/cgi-bin/imagemap...|   302|         110|
//|port26.annex2.nwl...| 01/Jul/1995|/images/construct...|   200|        1414|
//|www-a1.proxy.aol.com| 02/Jul/1995| /shuttle/countdown/|   200|        3985|
//|www-a1.proxy.aol.com| 02/Jul/1995| /shuttle/countdown/|      |            |
//|www-a1.proxy.aol.com| 02/Jul/1995|   /shuttle/countUp/|   200|           1|
// ]+--------------------+------------+--------------------+------+------------+
