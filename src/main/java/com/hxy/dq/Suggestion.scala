package com.hxy.dq

import org.apache.spark.sql.SparkSession
import java.io._
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}
object suggestion {
  def main(args: Array[String]) {

    val spark = SparkSession.builder
      .appName("Deequ")
      .master("local[*]")
      .getOrCreate()
    import spark.implicits._
    val dF = spark.read
      .option("header", true)
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
      .csv("/Users/mac/xp/projects/cscw/NewBpm/JumpFlow/titanic.csv")//"C:/Users/FT_N/Desktop/deequ-master/titanic.csv"

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
    suggestionDataFrame.toJSON.show()
    suggestionDataFrame.coalesce(1).write.option("header", "true").csv("/Users/mac/xp/projects/cscw/NewBpm/JumpFlow/sample_file1.csv")
//    suggestionDataFrame.write.csv("test.csv")
//    suggestionDataFrame.write.json("test.json")
  }
}
