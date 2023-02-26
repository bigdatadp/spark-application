package com.config
import com.typesafe.config.{Config, ConfigFactory}
import java.io.InputStreamReader
import org.apache.hadoop.fs.{FileSystem, Path}
class AppConfig(env: String, path: String){

  def getConfig(): Config ={
    if(this.env == "local"){
      val config = ConfigFactory.load(this.path)
      return config
    }else{
      val fs = FileSystem.get(new org.apache.hadoop.conf.Configuration())
      val reader = new InputStreamReader(fs.open(new Path(this.path)))
      val config = try {
        ConfigFactory.parseReader(reader)
      } finally {
        reader.close()
      }
      return config
    }


  }
}
