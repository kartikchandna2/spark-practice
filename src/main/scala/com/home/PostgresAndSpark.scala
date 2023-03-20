package com.home

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

import java.util.Properties

object PostgresAndSpark extends App{
  val sc = SparkSession.builder()
    .appName("PostgresAndSpark")
    .master("local")
    .getOrCreate()

  sc.sparkContext.setLogLevel("ERROR")
  println("spark session created")
  val connectionProperties = new Properties()
  connectionProperties.setProperty("Driver", "org.postgresql.Driver")
  connectionProperties.setProperty("user", "postgres")
  connectionProperties.setProperty("password", "Kartik@800")
  println("connection established")
  val pgTable = "public.t1"
  val df = sc.read.jdbc("jdbc:postgresql://localhost/testing?user=postgres&password=Kartik@800", pgTable, connectionProperties)
  df.show()

  //val df = sc.read
    //.format("jdbc")
    //.option("url", "jdbc:postgresql://localhost/testing?user=postgres&password=Kartik@800")
    //.option("dbtable", "t1")
    //.option("user", "postgres")
    //.option("password", "Kartik@800")
    //.load()

  //val url = "jdbc:postgresql://localhost/testing?user=postgres&password=Kartik@800"
  //val connectionProperties = new Properties()
  //connectionProperties.setProperty("Driver", "org.postgresql.Driver")
  //val query1 = "(SELECT * FROM t1) as q1"

}
