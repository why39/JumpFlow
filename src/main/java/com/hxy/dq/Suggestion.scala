package com.hxy.dq

import org.apache.spark.sql.{DataFrame, SparkSession}
import java.io._
import java.util.{Properties, UUID}

import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import com.amazon.deequ.VerificationSuite
import com.amazon.deequ.checks.{Check, CheckLevel, CheckStatus}
import com.amazon.deequ.constraints.{ConstrainableDataTypes, ConstraintStatus}
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}

object Suggestion {
  val path = "/Users/mac/xp/JumpFlow/case/"
  val spark = SparkSession.builder
    .appName("Deequ")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  def main(args: Array[String]) {
    dealAKX(path + "res_" + UUID.randomUUID())
  }

  def deal(dF: DataFrame, check: Check, outputname: String): Unit = {
    val spark = SparkSession.builder
      .appName("Deequ")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._

    val properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "19921015")


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
        check
      )

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

  //嫌疑人表
  def dealXYR(outputname: String): Unit = {


    val properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "19921015")

    val dF = spark.read
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .jdbc("jdbc:mysql://localhost:3306/hxyframe_activiti?allowMultiQueries=true&useUnicode=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true", "tb_dq_fzxyr", properties)
      .select("id", "xyrxm", "xyrbh", "afsnl", "xb")

    dF.printSchema()


    var check = Check(CheckLevel.Error, "person verification")
      .isComplete("id")
      .isUnique("id")
      .hasDataType("id", ConstrainableDataTypes.Integral)

      .isComplete("xyrxm")
      .hasDataType("xyrxm", ConstrainableDataTypes.String)

      .isComplete("xyrbh")
      .hasDataType("xyrbh", ConstrainableDataTypes.String)

      .isComplete("afsnl")
      .isNonNegative("afsnl")
      .isLessThan("afsnl", "120")
      .hasDataType("afsnl", ConstrainableDataTypes.Integral)

      .isComplete("xb")
      .isContainedIn("xb", Array("male", "female"))
      .hasDataType("xb", ConstrainableDataTypes.String)

    deal(dF, check, outputname)

  }


  //嫌疑人表
  def dealAKX(outputname: String): Unit = {


    val properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "19921015")

    val dF = spark.read
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .jdbc("jdbc:mysql://localhost:3306/hxyframe_activiti?allowMultiQueries=true&useUnicode=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true", "tb_dq_ak", properties)
      .select("id", "AFD_MC", "AJCBDW_MC", "AJCBDW_BM", "AJLB_MC", "AJLB_BM", "AY_MC", "AY_DM")

    dF.printSchema()


    var check = Check(CheckLevel.Error, "case verification")
      .isComplete("id")
      .isUnique("id")
      .hasDataType("id", ConstrainableDataTypes.Integral)

      .isComplete("AFD_MC")
      .hasDataType("AFD_MC", ConstrainableDataTypes.String)

      .isComplete("AJCBDW_MC")
      .hasDataType("AJCBDW_MC", ConstrainableDataTypes.String)

      .isComplete("AJCBDW_BM")
      .isNonNegative("AJCBDW_BM")
      .hasDataType("AJCBDW_BM", ConstrainableDataTypes.Integral)

      .isComplete("AJLB_MC")
      .hasDataType("AJLB_MC", ConstrainableDataTypes.String)

      .isComplete("AY_MC")
      .hasDataType("AJLB_MC", ConstrainableDataTypes.String)

      .isComplete("AY_DM")
      .isNonNegative("AY_DM")
      .hasDataType("AY_DM", ConstrainableDataTypes.Integral)


    deal(dF, check, outputname)

  }
}
