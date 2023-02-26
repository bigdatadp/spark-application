package com.sinks
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import java.util.concurrent.Future


class KafkaSink[K, V](createProducer: () => KafkaProducer[K, V]) extends Serializable {

  /* This is the key idea that allows us to work around running into
     NotSerializableExceptions. */
  lazy val producer = createProducer()

  def send(topic: String, key: K, value: V): Future[RecordMetadata] =
    producer.send(new ProducerRecord[K, V](topic, key, value))

  def send(topic: String, value: V): Future[RecordMetadata] =
    producer.send(new ProducerRecord[K, V](topic, value))
}

object KafkaSink {
  import scala.collection.JavaConversions._
  def apply(config: Map[String, Object]): KafkaSink[String, String] = {
    val f = () => {
      val producer = new KafkaProducer[String, String](config)
      sys.addShutdownHook {
        producer.close()
      }
      producer
    }
    new KafkaSink(f)
  }
//  def apply[K, V](config: java.util.Properties): KafkaSink[K, V] = apply(config.toMap)
}

