package jp.co.who.socket.server

import java.net.InetSocketAddress

import akka.actor.{ ActorRef, ActorSystem }

trait UDPServerTrait {

  var address = "127.0.0.1"
  var port    = 18081

  def actor(system: ActorSystem, inetSocketAddress: InetSocketAddress): ActorRef

  def main(args: Array[String]): Unit = {
    val socketAddr = new InetSocketAddress(address, port)
    val system     = ActorSystem("UDPSocket")
    actor(system, socketAddr)
  }

}
