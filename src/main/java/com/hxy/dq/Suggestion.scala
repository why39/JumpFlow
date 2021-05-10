package com.hxy.dq

import org.apache.spark.sql.SparkSession
import java.io._
import java.util.{Properties, UUID}

import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import com.amazon.deequ.VerificationSuite
import com.amazon.deequ.checks.{Check, CheckLevel, CheckStatus}
import com.amazon.deequ.constraints.ConstraintStatus
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}

object Suggestion {
  val path = "/Users/mac/xp/JumpFlow/case/"

  def main(args: Array[String]) {
    deal(path+"sample_file" + UUID.randomUUID())
  }

  def deal(outputname: String): Unit = {
    val spark = SparkSession.builder
      .appName("Deequ")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._

    //    val dF = spark.read
    //      .option("header", true)
    //      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
    //      .csv(path+"titanic.csv")

    val properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "19921015")

    val dF = spark.read
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .jdbc("jdbc:mysql://localhost:3306/hxyframe_activiti?allowMultiQueries=true&useUnicode=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true", "properties_mapper", properties)
      .select("en", "cn")

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
    suggestionDataFrame.show(truncate = false)


    val verificationResult = VerificationSuite()
      .onData(dF)
      .addCheck(
        Check(CheckLevel.Error, "DQ verification")
          .hasSize(_ > 0)
          .isComplete("en")
          .isUnique("en")
          .isComplete("en")
          .isNonNegative("en"))
      .run()

    if (verificationResult.status == CheckStatus.Success) {
      println("The data passed the test, everything is fine!")
    } else {
      println("We found errors in the data:\n")

      val resultsForAllConstraints = verificationResult.checkResults
        .flatMap { case (_, checkResult) => checkResult.constraintResults }

      resultsForAllConstraints
        .filter {
          _.status != ConstraintStatus.Success
        }
        .foreach { result => println(s"${result.constraint}: ${result.message.get}") }
    }
    val resultDataFrame = checkResultsAsDataFrame(spark, verificationResult)
    resultDataFrame.coalesce(1).write.format("csv")
      .option("header", "false")
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .mode("Append").save(outputname)
    resultDataFrame.coalesce(1).write
      .option("header", "true")
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .mode("Append").json(outputname)

  }
}
