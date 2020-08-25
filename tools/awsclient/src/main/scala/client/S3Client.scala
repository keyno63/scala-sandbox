package client

import collection.JavaConverters._
import com.amazonaws.auth.{ AWSStaticCredentialsProvider, BasicAWSCredentials }
import com.amazonaws.services.s3.model.Bucket
import com.amazonaws.services.s3.{ AmazonS3, AmazonS3ClientBuilder }

trait S3Client {
  val region: String
  val bucketName: String
  protected val s3: AmazonS3
  def getBuckets: List[Bucket] = s3.listBuckets().asScala.toList
}

object S3Client {
  def s3Client(accessKey: String, secretKey: String, regionValue: String, bucket: String): S3Client = new S3Client {
    val region: String     = regionValue
    val bucketName: String = bucket
    val credentials        = new BasicAWSCredentials(accessKey, secretKey)
    val provider           = new AWSStaticCredentialsProvider(credentials)
    val s3: AmazonS3 = AmazonS3ClientBuilder.standard
      .withCredentials(provider)
      .withRegion(region)
      .build();
  }
}
