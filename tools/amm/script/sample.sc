import skinny.http.{HTTP, Request}

@main
def hello(apiKey: String): Unit = {
  println(s"Hello, Hello!")
  val url = "https://language.googleapis.com/v1/documents:analyzeEntities"
  val _map = Map("key" -> apiKey).map{ item =>val (k,v) = item; s"$k=$v"}
  val _url = s"${url}?${_map}"
  val request = Request(url)
  val response = HTTP.post(request)
  println(s"${response.status}: ${response.body}")
}


