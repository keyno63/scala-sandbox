import client.S3Client
import config.aws.S3Property

object AwsSample extends scala.App {

  println(S3Property.secretKey)
  val s3 = S3Client.s3Client(
    S3Property.accessKey,
    S3Property.secretKey,
    S3Property.region,
    S3Property.bucketName
  )
  println(s3.getBuckets)

}
