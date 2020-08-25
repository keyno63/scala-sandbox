package config.aws

import com.typesafe.config.ConfigFactory

object S3Property {
  private val config = ConfigFactory.load()
  val accessKey: String = config.getString("aws.s3.access-key")
  val secretKey: String = config.getString("aws.s3.secret-key")
  val region: String = config.getString("aws.s3.region")
  val bucketName: String = config.getString("aws.s3.bucketName")
}
