import client.S3Client
import com.typesafe.config._

object AwsSample extends scala.App {

  // typesafe config
  val config = ConfigFactory.load()

  val accessKey = config.getString("aws.s3.access-key")
  val secretKey = config.getString("aws.s3.secret-key")
  val region = "us-east-2"
  val bucketName = "kofujiw-s3bucket"
  println(accessKey + secretKey)
  val s3 = S3Client.s3Client(accessKey, secretKey, region, bucketName)
  println(s3.getBuckets)

}
