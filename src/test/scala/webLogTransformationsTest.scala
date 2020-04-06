import logAnalysis.webLogTransformations
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.apache.spark.sql
import org.apache.spark.sql.{Row, SparkSession}


class webLogTransformationsTest extends FunSuite with BeforeAndAfterAll {

  val spark: SparkSession = SparkSession.builder.master("local[2]").appName("AnalyzeLogs").getOrCreate()

  import spark.implicits._

  val sampleData: sql.DataFrame = Seq(
    "199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] \"GET /history/apollo/ HTTP/1.0\" 200 6245",
    "unicomp6.unicomp.net - - [01/Jul/1995:00:00:06 -0400] \"GET /images/KSC-logosmall.gif HTTP/1.0\" 200 3985",
    "205.189.154.54 - - [01/Jul/1995:00:01:06 -0400] \"GET /cgi-bin/imagemap/countdown?99,176 HTTP/1.0\" 302 110",
    "port26.annex2.nwlink.com - - [01/Jul/1995:00:01:04 -0400] \"GET /images/construct.gif HTTP/1.0\" 200 1414",
    "www-a1.proxy.aol.com - - [02/Jul/1995:00:01:09 -0400] \"GET /shuttle/countdown/ HTTP/1.0\" 200 3985",
    "www-a1.proxy.aol.com - - [02/Jul/1995:00:01:09 -0400] \"GET /shuttle/countdown/ HTTP/1.0\" ",
    "www-a1.proxy.aol.com - - [02/Jul/1995:00:01:09 -0400] \"GET /shuttle/countUp/ HTTP/1.0\" 200 1",
    "205.189.154.54 - - [01/Jul/1995:00:00:24 -0400] \"GET /shuttle/countdown/ HTTP/1.0\" 200 3985",
    "waters-gw.starway.net.au - - [01/Jul/1995:00:00:25 -0400] \"GET /shuttle/missions/51-l/mission-51-l.html HTTP/1.0\" 200 6723",
    "ppp-mia-30.shadow.net - - [01/Jul/1995:00:00:27 -0400] \"GET / HTTP/1.0\" 200 7074"
  ).toDF


  val logAnalysisTest = new webLogTransformations
  val parseLog: sql.DataFrame = logAnalysisTest.readAndParse(sampleData)
  val transformedLog: sql.DataFrame = logAnalysisTest.transformData(parseLog, 2)


  override def afterAll: Unit = {
    spark.stop()
  }


  /** This test ensures that count of input and output are same */
  test("readAndParse method should return output count same as input count") {
    assert(parseLog.count() === 9)
  }


  /** This test ensures that parsing of columns is working as expected */
  test("readAndParse method should read a DataFrame and return parsed Data") {
    assert(parseLog.select("host").take(2) === Array(Row("199.72.81.55"), Row("unicomp6.unicomp.net")))
    assert(parseLog.select("receive_date").take(2) === Array(Row("01/Jul/1995"), Row("01/Jul/1995")))
    assert(parseLog.select("url").take(2) === Array(Row("/history/apollo/"), Row("/images/KSC-logosmall.gif")))
  }


  /** This test checks for the  expected visitor count result */
  test("transformData method should read a DataFrame and return frequent visitors data") {
    assert(transformedLog.filter("host= 'www-a1.proxy.aol.com'").select("no_of_times_visited").first() === Row(2))
    assert(transformedLog.filter("host= 'www-a1.proxy.aol.com'").count() === 1)
  }


  /** This test checks there are no URL's with nulls in the output */
  test("transformData method should read a DataFrame and return output without nulls") {
    assert(transformedLog.filter("host= 'ppp-mia-30.shadow.net'").count() === 0)
  }


}
