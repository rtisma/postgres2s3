package bio.overture.backup.postgres2s3.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Slf4j
@Setter
@Getter
@Component
@Validated
@ConfigurationProperties(S3Properties.S3)
public class S3Properties {

  public static final String S3 = "s3";

  @NotBlank private String accessKey;

  @NotBlank private String secretKey;

  @NotBlank
  @Pattern(regexp = "^[^\\.\\/]+[a-zA-Z0-9_\\-\\.]+$")
  private String bucketName;

  @NotBlank
  @Pattern(regexp = "^[^\\.\\/]+[a-zA-Z0-9_\\-\\.\\/]+$")
  private String bucketPath;

  private String endpointUrl;

}
