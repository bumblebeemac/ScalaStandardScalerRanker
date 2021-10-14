package Examples

import StandardScalerRanker.getStandardRankDF
import org.apache.spark.sql.{SaveMode, SparkSession}

object Example extends App {

  val spark = SparkSession.builder()
    .appName("Example")
    .master("local")
    .getOrCreate()

  // Set in order to make sure Exception does not occur due to string concatenation
  spark.conf.set("spark.debug.maxToStringFields", 1000)

  val aDF = spark.read
    .format("csv")
    .option("header", "true")
    .option("sep", ",")
    .csv("src/data/usedcars_dataset.csv")

  val getMeanStd = getStandardRankDF(aDF, grpByList = List("make", "aspiration", "num_of_doors", "body_style"),
    stdFactors = List("compression_ratio", "horsepower", "peak_rpm", "city_mpg", "highway_mpg", "price"))
  getMeanStd.show()
  getMeanStd.coalesce(1).write
    .option("header", "true")
    .option("sep", ",")
    .mode(SaveMode.Overwrite)
    .csv("src/data/getMeanStdResult.csv")

}
