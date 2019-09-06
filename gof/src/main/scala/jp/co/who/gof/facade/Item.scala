package jp.co.who.gof.facade

case class Item(name: String, price: Int)

case class Shop[T <: Item](name: String) {
  private var list = List[T]()
  def addItem(item: Item): Unit = {
    list :+= item
  }
}

object OrderSystem {

}
