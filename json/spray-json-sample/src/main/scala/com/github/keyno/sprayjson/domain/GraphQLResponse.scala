package com.github.keyno.sprayjson.domain

import com.github.keyno.sprayjson.domain.Other.ObjectValue

case class GraphQLResponse[+E](data: ResponseValue, errors: List[E], extentions: Option[ObjectValue] = None)

object GraphQLResponse {
  // encoder/decoder
}
