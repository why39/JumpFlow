package com.hxy.dq

import org.apache.spark.sql.SparkSession
import java.io._
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}
object Suggestion {
  val path = "/Users/mac/xp/projects/cscw/NewBpm/JumpFlow/"
  def main(args: Array[String]) {
    deal("sample_file1.csv")
  }

  def deal(outputname:String): Unit ={
    val spark = SparkSession.builder
      .appName("Deequ")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._
    val dF = spark.read
      .option("header", true)
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .csv(path+"titanic.csv")


    dF.printSchema()

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
    //    suggestionDataFrame.toJSON.show()
    suggestionDataFrame.coalesce(1).write
      .option("header", "true")
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .csv(path+"case/"+outputname)
    //    suggestionDataFrame.write.csv("test.csv")
    //    suggestionDataFrame.write.json("test.json")
  }
}
