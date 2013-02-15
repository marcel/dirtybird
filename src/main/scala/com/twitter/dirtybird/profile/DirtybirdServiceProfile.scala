package com.twitter.dirtybird
package profile

import com.twitter.servo.service.ServiceProfile
import com.twitter.decider.DeciderFactory
import com.twitter.finagle.thrift.ClientId
import com.twitter.util.TimeConversions._
import com.twitter.util.Config
import com.twitter.ostrich.admin.RuntimeEnvironment
import com.twitter.logging._
import com.twitter.logging.LoggerFactory
import scala.Some

class DirtybirdServiceProfile extends Config[RuntimeEnvironment => DirtybirdServer] with ServiceProfile[DirtybirdServer] {
  val name = "dirtybird"
  val rootDir = System.getProperty("user.dir")

  def fileHandler(filename: String) =
    ThrottledHandler(
      QueueingHandler(
        FileHandler(
          filename = rootDir + "/" + filename,
          rollPolicy = Policy.SigHup
        ),
        maxQueueSize = 1000
      ),
      duration = 5.seconds,
      maxToDisplay = 3
    )

  override def loggers =
    LoggerFactory(
      level = Some(Level.INFO),
      handlers = fileHandler("server.log") :: Nil
    )

  val shutdownGracePeriod = 15.seconds
  def servicePort = 9999
  def adminPort = 9990
  def thriftClientId = ClientId(name)
  def deciderFactory = DeciderFactory()
  def build() = new DirtybirdServer(this)

  def apply() = _ => build()
}
