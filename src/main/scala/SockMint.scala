import org.apache.spark.sql.SparkSession
import org.apache.hudi.QuickstartUtils._
import scala.collection.JavaConversions._
import org.apache.spark.sql.SaveMode._
import org.apache.hudi.DataSourceReadOptions._
import org.apache.hudi.DataSourceWriteOptions._
import org.apache.hudi.config.HoodieWriteConfig._
import org.apache.hudi.common.model.HoodieRecord

object SimpleApp {
  def main(args: Array[String]) {
    val logFile =
      "YOUR_SPARK_HOME/README.md" // Should be some file on your system
    val spark = SparkSession.builder.appName("Simple Application").getOrCreate()

    val tableName = "hudi_trips_cow"
    val basePath = "hdfs://localhost:9001/hieu2/hudi_trips_cow"
    val dataGen = new DataGenerator

    val inserts = convertToStringList(dataGen.generateInserts(10))
    val df = spark.read.json(spark.sparkContext.parallelize(inserts, 2))
    df.write
      .format("hudi")
      .options(getQuickstartWriteConfigs)
      .option(PRECOMBINE_FIELD_OPT_KEY, "ts")
      .option(RECORDKEY_FIELD_OPT_KEY, "uuid")
      .option(PARTITIONPATH_FIELD_OPT_KEY, "partitionpath")
      .option(org.apache.hudi.config.HoodieWriteConfig.TABLE_NAME, tableName)
      .mode(Overwrite)
      .save(basePath)
    spark.stop()
  }
}
