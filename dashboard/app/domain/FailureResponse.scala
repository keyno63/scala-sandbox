package domain

sealed trait FailureResponse
case class FailedParseJson(reason: String)   extends FailureResponse
case class FailedHttpRequest(reason: String) extends FailureResponse
case class FailedParams(reason: String)      extends FailureResponse
