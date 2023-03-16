package com.home
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object BikeDataPractice extends App{
  //another way to create a spark context is by setting the spark config first

  //val conf = new SparkConf().setAppName("BikeDataPractice").setMaster("local")
  //val sc = new SparkContext(conf)


  //create a spark context object
  val sc = new SparkContext("local", "BikeDataPractice")

  //load the data in an RDD
  val dataRdd = sc.textFile("src/resources/Used_Bikes.csv")

  //split the csv on ',' and trim the columns
  val splitData = dataRdd.map(lines => lines.split(",").map(column => column.trim))

  //print each object's first column for each line
  splitData.map(model => model(0)).foreach(println)

  //First Owner, Yamaha bike with CC > 150
  val resultRDD = splitData.filter(bike => bike(4).equalsIgnoreCase("First Owner")
                    && bike(6).toDouble > 150 && bike(7).equalsIgnoreCase("Yamaha"))

  resultRDD.map(model => model(0)).distinct().foreach(println)
  val c = resultRDD.count()
  println(c)
}
