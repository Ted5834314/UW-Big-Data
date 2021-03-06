package ca.uwaterloo.cs451.a5

import org.apache.log4j._
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.rogach.scallop._
import org.apache.spark.sql.SparkSession

class Conf(args: Seq[String]) extends ScallopConf(args) {
  mainOptions = Seq(input, date, text, parquet)
  val input = opt[String](descr = "input path", required = true)
  val date = opt[String](descr = "date", required = false)
  val text = opt[Boolean](descr = "text", required = false)
  val parquet = opt[Boolean](descr = "parquet", required = false)
  verify()
}

object Q1  {
	val log = Logger.getLogger(getClass().getName())

	def main(argv: Array[String]) {
	    val args = new Conf(argv)
            log.info("Input: " + args.input())
	    val conf = new SparkConf().setAppName("Q1")
            val sc = new SparkContext(conf)
	    val date = args.date()
            if (args.text()) {
              val text = sc.textFile(args.input() + "/lineitem.tbl")
              val count = text
             .map(line => line.split("\\|")(10))
             .filter(_.contains(date))
             .count

             println("ANSWER=" + count)
            } else if (args.parquet()) {
             val sparkSession = SparkSession.builder.getOrCreate 
             val df= sparkSession.read.parquet(args.input() + "/lineitem")
             val rdd= df.rdd
  	    val count = rdd
  			.map(line => line.getString(10))
  			.filter(_.contains(date))
  			.count

  		println("ANSWER=" + count)
    }
	}
}
