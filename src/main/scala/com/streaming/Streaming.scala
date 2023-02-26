package com.streaming

import com.config.AppConfig
import com.logging.AppLog
import com.mongodb.spark._
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import com.sources.KafkaSources
import com.sinks.KafkaSink
import org.apache.kafka.clients.producer.ProducerConfig
//import collection.mutable.Map
import scala.collection.immutable.HashMap
object Streaming extends AppLog{
  def main(args: Array[String]): Unit = {
    val env = args(0) //local, dev, staging, production
    val configPath = args (1)
    val config: AppConfig = new AppConfig(env, configPath)
    var configContext: Config = config.getConfig()
    val appName = configContext.getString("appname")
    val master = configContext.getString("master")
    val topics = configContext.getString("kafka.topics")
    val sources = configContext.getString("kafka.source")
    val sink = configContext.getString("kafka.sink")
    val conf = new SparkConf().setMaster(master).setAppName(appName)
    @transient val ssc = new StreamingContext(conf, Seconds(1))

    val sc = ssc.sparkContext
    import scala.collection.JavaConversions._
    val props: Map[String, Object] = Map[String, Object](
      ProducerConfig.BOOTSTRAP_SERVERS_CONFIG -> sink,
      ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG ->
            "org.apache.kafka.common.serialization.StringSerializer",
      ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringSerializer"
    )
//    props.put(, sink)
//    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//      "org.apache.kafka.common.serialization.StringSerializer")
//    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//      "org.apache.kafka.common.serialization.StringSerializer")
    val kafkaSink = sc.broadcast(KafkaSink(props))
    sc.setLogLevel("WARN")
    val messages = new KafkaSources(ssc, topics, sources, "reddit-local-02").getStream(kafkaSink)
//    //read config from file and load to stream config
    ssc.start()
    ssc.awaitTermination()
  }
}
