import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

import scala.annotation.tailrec

package object StandardScalerRanker {

  /**
   * @author Ravishankar Subramanian
   * @param inputDF DataFrame with columns to be standardized and scored
   * @param grpByList column in DataFrame to groupBy and calculate mean and standard deviation
   * @param stdFactors columns in DataFrame that need tp be standardized and scored
   * @param dropCols if true, mean and standard deviation of standardized are dropped
   * @return DataFrame with input DataFrame with score and rank columns
   */
  def getStandardRankDF(inputDF: DataFrame, grpByList: List[String], stdFactors: List[String],
                        dropCols: Boolean = true, calcRankScore: Boolean = true): DataFrame = {

    // Convert stdFactors to iterator | concatenated column in inputDF of stdFactors columns
    val normIterFactors = stdFactors.iterator
    val concatCols = grpByList.map(col)
    val concatColName = "grpByConcat"
    val concatDF = inputDF.withColumn(concatColName, concat_ws("_", concatCols: _*))

    // Fill all null values with 0 before standardizing to avoid values being ignored
    val mergedDFFill = concatDF.na.fill(stdFactors.map(x => (x, 0.0)).toMap)

    def meanStdCalc(df: DataFrame, column: String): DataFrame = {
      val meanStdDF = df.groupBy(concatColName).agg(mean(column).as("mean_" + column),
        stddev_pop(column).as("stddev_" + column))
      val meanStdFinalDF = meanStdDF.withColumnRenamed(concatColName, concatColName + "_" + column)
      val finalDF = df.join(meanStdFinalDF, df.col(concatColName) === meanStdFinalDF
        .col(concatColName + "_" + column), joinType = "left")
      finalDF
    }

    val finalDF = stdFactors.foldLeft(mergedDFFill)(meanStdCalc)

    val stdrzdColAddFunc: (DataFrame, String) => DataFrame = (x, y) => x.withColumn("norm_" + y,
      (col(y) - col("mean_" + y)) / col("stddev_" + y))

    def calcStandardizedCols(df: DataFrame, factors: Iterator[String]): DataFrame = {
      @tailrec
      def recursorHelper(acc: DataFrame): DataFrame = {
        if (!factors.hasNext) acc
        else recursorHelper(stdrzdColAddFunc(acc, factors.next()))
      }

      recursorHelper(df)
    }

    val intrmResultDF = calcStandardizedCols(finalDF, normIterFactors)
    val resultDF = intrmResultDF.na.fill(stdFactors.map("norm_" + _).map(x => (x, 0)).toMap)

    val finalResult1DF = if (calcRankScore) {
      val _listCols = stdFactors.map(x => col("norm_" + x)).mkString(" + ")
      val calcScoreDF = resultDF.withColumn("score", expr(_listCols))
      val calcRankDF = calcScoreDF.withColumn("rank", rank().over(Window.partitionBy(concatColName)
        .orderBy(desc("score"))))
      calcRankDF
    } else resultDF

    val finalResult2DF = if (dropCols) {
      val removeCols = stdFactors.flatMap(x => List(grpByList + "_" + x, "mean_" + x, "stddev_" + x, "norm_" + x,
        "grpByConcat_"+ x))
      finalResult1DF.drop(removeCols: _*)
    } else finalResult1DF

    finalResult2DF
  }

}
