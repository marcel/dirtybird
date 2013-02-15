package com.twitter.dirtybird

import com.twitter.logging.Logger
import com.twitter.ostrich.admin.RuntimeEnvironment

object Main {
  val log = Logger.get(getClass.getSimpleName)

  def main(args: Array[String]) {
    val runtime = RuntimeEnvironment(this, args)
    val server = runtime.loadRuntimeConfig[DirtybirdServer]()

    log.info("Starting Dirtybird")

    try {
      server.start()
    } catch {
      case e: Exception =>
        log.error(e, "unexpected exception: %s", e.getMessage)
        System.exit(0)
    }
  }
}

