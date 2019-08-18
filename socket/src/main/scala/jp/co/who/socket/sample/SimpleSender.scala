package jp.co.who.socket.sample

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.io.{IO, Udp}
import akka.util.ByteString

class SimpleSender(remote: InetSocketAddress)
  extends Actor with ActorLogging {
  import context.system
  IO(Udp) ! Udp.SimpleSender

  def receive = {
    case Udp.SimpleSenderReady => {
      log.info(s"to send")
      context.become(ready(sender()))
    }
  }

  def ready(send: ActorRef): Receive = {
    case msg: String => {
      log.info(s"send ${msg}")
      send ! Udp.Send(ByteString(msg), remote)
    }
    case _ =>
      log.info(s"not send.")
  }

}

object SimpleSender extends App {

  val systemSender = ActorSystem("SimpleSender")
  val sock = new InetSocketAddress("localhost", 18081)
  val ac = systemSender.actorOf(Props(classOf[SimpleSender], sock), "SimpleSender")

  (0 to 100).foreach { i =>
    ac ! "hi SimpleSender"
    Thread.sleep(2000)
  }
  println("finished ")
}
