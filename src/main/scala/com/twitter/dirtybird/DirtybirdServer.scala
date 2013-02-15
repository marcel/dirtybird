package com.twitter.dirtybird

import com.twitter.finagle.builder.{Server => FinagleServer}
import profile.DirtybirdServiceProfile
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol
import com.twitter.ostrich.admin.Service
import com.twitter.logging.Logger
import com.twitter.servo.util.MemoizingStatsReceiver
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.thrift.ThriftServerFramedCodec

class DirtybirdServer(profile: DirtybirdServiceProfile) extends Service { server =>
  private[this] val logger = Logger.get(getClass.getSimpleName)

  @volatile private[this] var thriftServers: Seq[FinagleServer] = Nil

  val statsReceiver = new MemoizingStatsReceiver(profile.statsReceiver)

//  val decider                      = config.deciderConfig()

  val dirtybirdService = new DirtybirdService(statsReceiver)

  def start() {
    // TODO should this be profile.start?
    synchronized {
      val servicePort = profile.servicePort
      logger.info("attempting to start %s on port %d.", profile.name, servicePort)

      if (thriftServers.isEmpty) {
        thriftServers = Seq(
          createThriftServer(profile.servicePort)
//          createThriftServer(profile.servicePort + 1)
        )

//        config.serviceCluster foreach { _.join(servicePort) }

        logger.info("started %s on port %d.", profile.name, servicePort)
      }
    }
  }

  def shutdown() {
    synchronized {
      logger.info("shutting down %s.", profile.name)
      thriftServers foreach { server =>
        server.close(profile.shutdownGracePeriod)
        thriftServers = Nil
      }
    }
  }

  private[this] def createThriftServer(port: Int) = {
    val service = new thriftscala.DirtybirdService.FinagledService(
      dirtybirdService, new TBinaryProtocol.Factory()
    )

    ServerBuilder()
      .name("Dirtybird on %d".format(port))
      .reportTo(statsReceiver)
      .codec(ThriftServerFramedCodec())
      .bindTo(new InetSocketAddress(port))
      .build(service)
  }

}

