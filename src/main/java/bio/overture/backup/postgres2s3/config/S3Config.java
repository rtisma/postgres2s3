package bio.overture.backup.postgres2s3.config;

import bio.overture.backup.postgres2s3.properties.S3Properties;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.google.common.base.Strings.isNullOrEmpty;

@Slf4j
@Configuration
public class S3Config {

  private final S3Properties s3Properties;

  @Autowired
  public S3Config(@NonNull S3Properties s3Properties) {
    this.s3Properties = s3Properties;
  }

  @Bean
  public AmazonS3 amazonS3Client(){
    val client = new AmazonS3Client(
        new BasicAWSCredentials(s3Properties.getAccessKey(), s3Properties.getSecretKey()),
        clientConfiguration());
    if (!isNullOrEmpty(s3Properties.getEndpointUrl())){
      client.setEndpoint(s3Properties.getEndpointUrl());
    }
    client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
    return client;
  }

  private static ClientConfiguration clientConfiguration() {
    ClientConfiguration clientConfiguration = new ClientConfiguration();

    clientConfiguration.setSignerOverride("AWSS3V4SignerType");
    log.info("Using AWSS3V4SignerType");

    clientConfiguration.setProtocol(Protocol.HTTPS);
    clientConfiguration.setRetryPolicy(
        PredefinedRetryPolicies.getDefaultRetryPolicyWithCustomMaxRetries(5));
    clientConfiguration.setConnectionTimeout(150000);
    return clientConfiguration;
  }

}
