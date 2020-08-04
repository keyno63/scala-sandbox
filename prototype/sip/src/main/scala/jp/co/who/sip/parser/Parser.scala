package jp.co.who.sip.parser

class Parser {

  def sample(data: Any): Any = data match {
    case s: String => s
    case _         => Some(data)
  }

}

object Parser {

  private val INVITE =
    """
      |INVITE sip:1201-2-34(5)6@192.168.6.221 SIP/2.0
      |Via: SIP/2.0/TCP 192.168.1.100:5060;branch=z9hG4bK111111111
      |Via: SIP/2.0/UDP 192.168.1.200:5060;branch=z9hG4bK222222222
      |From: sip:05011112222@192.168.1.10;tag=54b866ac
      |To: <sip:1201-2-3456@192.168.1.251>
      |Call-ID: 2e9d78fe-0427dd31-829d1b41@sample.com
      |CSeq: 1 INVITE
      |Max-Forwards: 70
      |Record-Route: <sip:192.168.1.200:5060;transport=tcp;lr>
      |Contact: <sip:05016001234@192.168.1.99>
      |Require: 100rel,timer
      |Session-Expires: 180
      |Allow: INVITE,CANCEL,ACK,BYE,INFO,OPTIONS,UPDATE
      |Supported: 100rel,timer
      |Content-Type: application/sdp
      |Content-Length: 127
      |
      |v=0
      |o=- 0 0 IN IP4 192.168.1.1
      |s=sipp
      |c=IN IP4 192.168.1.1
      |k=prompt
      |t=0 0
      |m=audio 11111 RTP/AVP 0
      |a=rtpmap:0 PCMU/8000
    """.stripMargin

  def main(args: Array[String]): Unit =
    println(INVITE split '\n')

  def sampleParser(): Parser = {
    val c = new Parser
    c.sample("something")
    c.sample(1)
    c
  }
}
