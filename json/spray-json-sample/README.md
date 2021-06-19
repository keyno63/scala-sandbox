# spray-json-sample

ライブラリに spray-json 対応を実装するにあたって  
試し書きをした

## やること概要

- `SprayJsonBackend` を実装できるようにする  
  以下のメソッドを書けるようにする
  - parseHttpRequest: => Either[Throwable, GraphQLRequest]
  - encodeGraphQLResponse: => String
  - parseWSMessage: => Either[Throwable, WSMessage]
  - encodeWSResponse: => String
  - encodeWSError: => String
  - reqUnmarshaller: => FromEntityUnmarshaller[GraphQLRequest]
- 各 case class の JSON 変換ができるようにする。  
  対象は以下
  - InputValue
  - Value
  - ResponseValue
  - GraphQLRequest
  - GraphQLResponse
- Encoder/Decoder の実装は `interop.sprayjson` に書く
- implicit object として、`RootJsonFormat[T]` の実装を書く  
その時の書き方としては以下
```scala
implicit object AFormat extends RootJsonFormat[A] {
  // JsonWriter[T] としての実装
  override def write(obj: A): JsValue =
    sprayjson.encoder(obj)
  // JsonReader[T] としての実装
  override def read(json: JsValue): A =
    sprayjson.decoder(json)
}
```
この implicit object をうまく渡せるようにして、  
Json <=> case class A の変換を行えるようにする

## spray-json 自体の使いかた
 - 流れ
   - object に DefaultJsonProtocol を継承する
   - JsonFormatter を定義する
     - in the case of case class,
       implicit val, from jsonFormatN
     - in the case of class,
       function write and read, in implicit object
       from JsArray(...) and logic to convert to class.
   - 実装する class/object で JsonFormatter を import する
   - Json への encode, decode を行い、JsValue を得る
 - できること
   - string => JsValue (.parseJson)
   - case class => JsValue (.toJson with implicit JsonFormatter)
   - JsValue => case class (.convertTo[case class] with JsonFormatter)
   - class => JsValue (.toJson)
   - JsValue => case class (.convertTo[class] with )
