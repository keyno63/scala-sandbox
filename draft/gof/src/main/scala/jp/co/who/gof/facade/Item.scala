package jp.co.who.gof.facade

import scala.collection.mutable

case class Item(name: String, price: Int)
case class Order(id: Int) {
  private var list = List[Item]()
  def addItem(item: Item): Unit =
    list :+= item

  def removeItem(item: Item): Unit = ???
}

case class Shop[T <: Item](name: String) {
  private var list = List[T]()
  def addItem(item: T): Unit =
    list :+= item
}

object OrderSystem {

  private var orders: mutable.HashMap[Int, Order] = new mutable.HashMap[Int, Order]
  def addOrder(order: Order): Unit =
    this.orders ++= mutable.HashMap(order.id -> order)
  def getOrder(id: Int): Order =
    this.orders(id)

}
