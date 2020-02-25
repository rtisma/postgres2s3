package bio.overture.backup.postgres2s3.service;

import bio.overture.backup.postgres2s3.properties.BackupProperties;
import bio.overture.backup.postgres2s3.properties.S3Properties;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
@Service
public class S3Service {

  private final S3Properties s3Properties;
  private final BackupProperties backupProperties;
  private final AmazonS3 amazonS3Client;

  @Autowired
  public S3Service(
      @NonNull S3Properties s3Properties,
      @NonNull BackupProperties backupProperties,
      @NonNull AmazonS3 amazonS3Client) {
    this.s3Properties = s3Properties;
    this.amazonS3Client = amazonS3Client;
    this.backupProperties = backupProperties;
  }

  public void uploadFile(@NonNull Path file){
    val bucketName = s3Properties.getBucketName();
    provisionBucket(bucketName);
    val key =  file.getFileName().toString();
    log.info("Uploading file '{}' to bucket '{}", file.toString(), bucketName);
    amazonS3Client.putObject(bucketName, key, file.toFile());
    log.info("-  Done");
  }

  private void provisionBucket(String bucketName){
    if(!backupProperties.isAutoCreateBucket()){
      checkBucketExists(bucketName);
    } else if (!isBucketExist(bucketName)){
      log.info("Creating non-existent bucket '{}'", bucketName);
      createBucket(bucketName);
      log.info("-  Done");
    }
  }

  private boolean isBucketExist(String bucketName){
    return amazonS3Client.listBuckets().stream().map(Bucket::getName).anyMatch(x -> x.equals(bucketName));
  }

  private void checkBucketExists(String bucketName){
    checkState(isBucketExist(bucketName), "The bucket '%s' does not exist", bucketName);
  }

  private void createBucket(String bucketName){
    amazonS3Client.createBucket(bucketName);
  }

}
