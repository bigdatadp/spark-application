package com.logging

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.Marker

abstract class AppLog{
  protected val parentLogger: Logger = LogManager.getLogger

  private var logger = parentLogger

  protected def getLogger: Logger = logger

  protected def setLogger(logger: Logger): Unit = {
    this.logger = logger
  }


  def log(marker: Marker): Unit = {
    logger.debug(marker, "Parent log message")
  }
}
