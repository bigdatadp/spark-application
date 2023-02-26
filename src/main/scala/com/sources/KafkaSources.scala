package com.sources

import com.logging.AppLog
import com.sinks.KafkaSink
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.InputDStream

class KafkaSources(ssc: StreamingContext, topics: String, brokers: String, groupId: String) extends AppLog {
  def getStream(kafkaSink: Broadcast[KafkaSink[String, String]], joltChains: Broadcast[String]) {
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer])
    val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams))
    messages.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd.foreachPartition { iter =>
        val o: OffsetRange = offsetRanges(0)
        println(s"Topic Name: ${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        //        this.getLogger.warn(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
        iter.foreach { record =>
          println(s"Sending record ${record.value()}")
          kafkaSink.value.send(, record.value())
        }
      }
    }

  }
}
