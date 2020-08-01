package jp.co.who.converse

object Converse extends App {

  val myHuman: MyType[Human] = new MyType(new Human)
  val myAnimal: MyType[Animal] = myHuman
}

class Animal
class Human extends Animal

//
class MyTypeConvariant[+A](a: A)
class MyTypeInvariant[-A](a: A)
class MyType[+A](a: A)
