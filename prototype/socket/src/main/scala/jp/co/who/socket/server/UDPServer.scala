package jp.co.who.socket.server

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.io.{IO, Udp}
import jp.co.who.socket.server.UDPServer.{address, port}

class UDPServer(inetSocketAddress: InetSocketAddress)
  extends Actor with ActorLogging
{

  import context.system

  IO(Udp) ! Udp.Bind(self, inetSocketAddress)

  def receive =
  {
    case Udp.Bound(local) =>
      val c = sender()
      println(s"receive. bound to ${local}:${c}")
      log.debug(s"bound to ${local}")
      context.become(ready(c))
  }

  def ready(socket: ActorRef): Receive =
  {
    case Udp.Received(data, remote) =>
      println(s"Received. bound to ${address}:${port}")
      socket ! Udp.Send(data, remote)
    case Udp.Unbind  =>
      println(s"Unbind. bound to ${address}:${port}")
      socket ! Udp.Unbind
    case Udp.Unbound =>
      println(s"Unbound. bound to ${address}:${port}")
      context.stop(self)
    case _ => println(s" Wild card. ${address}:${port}")
  }
}

object UDPServer extends UDPServerTrait
{
  override def actor(system: ActorSystem, inetSocketAddress: InetSocketAddress): ActorRef =
    system.actorOf(Props(classOf[UDPServer], inetSocketAddress), "UDPSocket")

  override def main(args: Array[String]): Unit =
  {
    super.main(args)
    println(s"bound to ${address}:${port}")
  }

}
