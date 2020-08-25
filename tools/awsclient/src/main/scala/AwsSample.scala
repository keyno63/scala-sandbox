import client.S3Client
import config.aws.S3Property

object AwsSample extends scala.App {

  val s3 = S3Client.s3Client(
    S3Property.accessKey,
    S3Property.secretKey,
    S3Property.region,
    S3Property.bucketName
  )
  println(s"s3 buckets: ${s3.getBuckets}")
}
