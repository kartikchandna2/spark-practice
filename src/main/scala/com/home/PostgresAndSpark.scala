package com.home

import org.apache.spark.SparkContext
import org.apache.spark.sql.types._ //for all data types

import org.apache.spark.sql.Row //programmatic schema

import org.apache.spark.sql.SparkSession

import org.apache.spark.sql.functions.udf //for user defined functions

import java.util.Properties

object PostgresAndSpark extends App{
  val sc = SparkSession.builder()
    .appName("PostgresAndSpark")
    .master("local")
    .getOrCreate()

  sc.sparkContext.setLogLevel("ERROR")
  println("spark session created")
  val connectionProperties = new Properties()
  connectionProperties.setProperty("Driver", "org.postgresql.Driver") // can also use put() instead of setProperty as
  connectionProperties.setProperty("user", "postgres") // put() is used for all objects and set/get Property for string
  connectionProperties.setProperty("password", "Kartik@800")
  println("connection established")
  val pgTable = "public.t1"
  val df = sc.read.jdbc("jdbc:postgresql://localhost/testing?user=postgres&password=Kartik@800", pgTable, connectionProperties)
  df.show()

  import sc.implicits._ //needed to use the $-notation

  df.printSchema()

  df.select("name").show()

  df.select($"id" + 1, $"name").show()

  df.filter($"id" > 2).show()

  df.groupBy("id").count().show()

  df.createOrReplaceTempView("people")
  //create a temporary view for this session and tie the dataframe to the table

  val tab = sc.sql("SELECT * FROM people")
  tab.show()

  tab.map(attr => "Name: " + attr(1)).show()

  //create new data frames using a case class
  case class Person(name: String, age: Long)

  //new dataframe
  val caseClass = Seq(Person("Andy", 32)).toDS()

  caseClass.show()

  //new dataframe
  val primitiveDS = Seq(1,2,3).toDS()

  primitiveDS.show()

  primitiveDS.collect().foreach(println)

  //create dataframe with a different view of the same class
  val userDefinedDF = sc.createDataFrame((1 to 10).map(i => Person(s"val_$i", i)))

  userDefinedDF.createOrReplaceTempView("person")

  println("Result of Select *: ")
  sc.sql("SELECT * FROM person").collect().foreach(println)

  //aggregation
  val count = sc.sql("SELECT COUNT(*) FROM person").collect().head.getLong(0)
  println(s"COUNT(*): $count")

  //can also be written as
  userDefinedDF.where($"age" === 1).orderBy($"name".asc).select($"age").collect().foreach(println)

  //create a user defined function
  val random = udf(() => Math.random()+1)
  sc.udf.register("random", random.asNondeterministic())
  sc.sql("select random()").show()

  //with one argument
  val plusOne = udf((x: Int) => x+1)
  sc.udf.register("plusOne", plusOne)
  sc.sql("select plusOne(5)").show()

  sc.stop()







}
