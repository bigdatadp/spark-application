import org.apache.spark.sql.SparkSession
import org.apache.hudi.QuickstartUtils._
import scala.collection.JavaConversions._
import org.apache.spark.sql.SaveMode._
import org.apache.hudi.DataSourceReadOptions._
import org.apache.hudi.DataSourceWriteOptions._
import org.apache.hudi.config.HoodieWriteConfig._
import org.apache.hudi.common.model.HoodieRecord
import play.api.libs.json._
import scala.concurrent._, duration._
import java.io.File
import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import java.io.IOException;
import java.util.List;
object JsonExtractor {
  def main(args: Array[String]) {
    val input_file = "./data.json"
    val chainrSpecJSON = JsonUtils.filepathToList( "./schema.json" );
    val chainr = Chainr.fromSpec( chainrSpecJSON );
    val inputJSON = JsonUtils.filepathToObject( "./data.json" );
    val transformedOutput = chainr.transform( inputJSON );
    println(JsonUtils.toJsonString(transformedOutput))
    
    // val jsonTransformer = (__ ).json.pickBranch
    // json.transform(jsonTransformer)
  }
//   def getListOfFiles(dir: String): List[File] = {
//     val d = new File(dir)
//     if (d.exists && d.isDirectory) {
//       d.listFiles.filter(_.isFile).toList
//     } else {
//       List[File]()
//     }
//   }
}
// import play.api.libs.json._

// scala> json.transform(jsonTransformer)
// res11: play.api.libs.json.JsResult[play.api.libs.json.JsObject] =
//   JsSuccess(
//   {
//     "key2": {
//       "key24":{
//         "key241":234.123
//       }
//     }
//   },
//   /key2/key24/key241
//   )
