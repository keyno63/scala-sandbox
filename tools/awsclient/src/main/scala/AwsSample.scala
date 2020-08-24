import client.S3Client

object AwsSample extends scala.App {

  val accessKey = ???
  val secretKey = ???
  val region = "us-east-2"
  val bucketName = "kofujiw-s3bucket"
  val s3 = S3Client.s3Client(accessKey, secretKey, region, bucketName)
  println(s3.getBuckets)

}
