package bio.overture.backup.postgres2s3.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class S3Service {

  private final AmazonS3 amazonS3Client;

  @Autowired
  public S3Service(@NonNull AmazonS3 amazonS3Client) {
    this.amazonS3Client = amazonS3Client;
  }

  public void uploadFile(@NonNull Path file){

  }

  public boolean isBucketExist(@NonNull String bucketName){
    return false;
  }

  public void checkBucketExists(@NonNull String bucketName){

  }

  public void createBucket(@NonNull String bucketName){

  }

}
