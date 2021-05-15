package com.hxy.dq

import org.apache.spark.sql.SparkSession
import com.amazon.deequ.VerificationSuite
import com.amazon.deequ.checks.{Check, CheckLevel, CheckStatus}
import com.amazon.deequ.constraints.ConstraintStatus
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}
import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import java.io._

object Testapp {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {

    val spark = SparkSession.builder
      .appName("Deequ")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._
    val dF = spark.read
      .option("header", true)
      .csv("/Users/mac/xp/JumpFlow/titanic.csv")

    dF.printSchema()

   // val writer = new PrintWriter(new File("test.txt"))

    val suggestionResult = {
      ConstraintSuggestionRunner()
        .onData(dF)
        .addConstraintRules(Rules.DEFAULT)
        .run()
    }

    val suggestionDataFrame = suggestionResult.constraintSuggestions.flatMap {
      case (column, suggestions) => suggestions.map {
        constraint => (column, constraint.description, constraint.codeForConstraint)

      }
    }.toSeq.toDS()
    suggestionDataFrame.show(truncate=false)


    val verificationResult = VerificationSuite()
      .onData(dF)
      .addCheck(
        Check(CheckLevel.Error, "DQ verification")
          .hasSize(_ > 0)
          .isComplete("Survived")
          .isUnique("Name")
          .isComplete("Age")
          .isContainedIn("Sex", Array("male", "female"))
          .isNonNegative("Survived"))
      . run()

    if (verificationResult.status == CheckStatus.Success) {
      println("The data passed the test, everything is fine!")
    } else {
      println("We found errors in the data:\n")

      val resultsForAllConstraints = verificationResult.checkResults
        .flatMap { case (_, checkResult) => checkResult.constraintResults }

      resultsForAllConstraints
        .filter { _.status != ConstraintStatus.Success }
        .foreach { result => println(s"${result.constraint}: ${result.message.get}") }
    }
    val resultDataFrame = checkResultsAsDataFrame(spark, verificationResult)
    resultDataFrame.coalesce(1).write.format("csv").option("header","false").mode("Append").save("C:/Users/FT_N/Desktop/deequ-master/atest")
    resultDataFrame.toJSON.collectAsList.toString
    resultDataFrame.coalesce(1).write.option("header", "true").mode("Append").json("C:/Users/FT_N/Desktop/deequ-master/atestj")
    //val df=resultDataFrame.withColumnRenamed("Review Check","ReviewCheck").withColumnRenamed("Input data does not include column nciks!","NoColNicks").withColumnRenamed("CompletenessConstraint(Completeness(nciks,None))","CompletenessConstraint")
    //df.coalesce(1).write.option("path","C:/Users/FT_N/Desktop/deequ-master/testj").mode("overwrite")saveAsTable("DeequVerification")

  }
}
