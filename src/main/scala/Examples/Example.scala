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

  val aDF = spark.read
    .format("csv")
    .option("header", "true")
    .option("sep", ",")
    .csv("/Users/bumblebeemac/Documents/usedcars_dataset.csv")

  val getMeanStd = getStandardRankDF(aDF, grpByList = List("make", "body-style"), stdFactors = List("engine-size",
    "horsepower", "peak-rpm", "city-mpg", "highway-mpg", "price"))
  getMeanStd.show()

}
