package jp.co.who.gcpclient.csc

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import java.io._

import jp.co.who.gcpclient.config.Config

object SampleCloudStrageClient extends Config {

  @throws[IOException]
  def main(args: Array[String]): Unit = {
    val storage =
      if (args.length > 0) getStorageFromJsonKey(args.head)
      else StorageOptions.getDefaultInstance.getService
    val bucket = storage.get(BUCKET)
    // 特定のディレクトリのみに絞る
    val option       = Storage.BlobListOption.prefix(PREFIX)
    val blobs        = bucket.list(option)
    val blobIterator = blobs.iterateAll
    blobIterator.forEach(x => println(x.getName))
  }

  @throws[IOException]
  private def getStorageFromJsonKey(key: String): Storage =
    StorageOptions.newBuilder
      .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(key)))
      .build
      .getService
}
