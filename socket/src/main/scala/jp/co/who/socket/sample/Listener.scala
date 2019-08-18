package jp.co.who.socket.sample

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.io.{IO, Udp}

class Listener extends Actor with ActorLogging {
  import context.system
  val port = 18081
  IO(Udp) ! Udp.Bind(self, new InetSocketAddress("192.168.1.148", port))
  log.info("bind")

  def receive = {
    case Udp.Bound(local) => {
      log.info(s"receive ${local}")
      context.become(ready(sender()))
    }
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      val processed = // parse data etc., e.g. using PipelineStage
        socket ! Udp.Send(data, remote) // example server echoes back
      //nextActor ! processed
      log.info(s"ready. Received $processed, $data, $remote")
    case Udp.Unbind  => {
      log.info("ready. Unbind")
      socket ! Udp.Unbind
    }
    case Udp.Unbound => {
      context.stop(self)
      log.info(s"ready. Unbound")
    }
  }
}

object Listener extends App {
  val system = ActorSystem("UDPSocket")
  val ac = system.actorOf(Props[Listener])
}
