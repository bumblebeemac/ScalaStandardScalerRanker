package Examples

import StandardScalerRanker.getStandardRankDF
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions.{col, concat_ws}

object Example extends App {

  val spark = SparkSession.builder()
    .appName("Example")
    .master("local")
    .getOrCreate()

  spark.conf.set("spark.debug.maxToStringFields", 1000)

//  val aDF = spark.read
//    .format("csv")
//    .option("header", "true")
//    .option("sep", ",")
//    .csv("/Users/bumblebeemac/Documents/usedcars_dataset.csv")

  val aSeq = Seq(
    ("aa", "A", 5.7, 2.1),
    ("aa", "B", 9.7, 7.4),
    ("aa", "C", 8.1, 5.9),
    ("bb", "X", 4.3, 1.1),
    ("bb", "Y", 2.3, 6.8),
    ("aa", "D", 6.6, 9.1)
  )

  import spark.implicits._
  val aDF = aSeq.toDF("A", "B", "C", "D")

  val getMeanStd = getStandardRankDF(aDF, grpByList = List("A"), stdFactors = List("C", "D"))
  getMeanStd.show()

}
