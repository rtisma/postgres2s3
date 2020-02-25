package bio.overture.backup.postgres2s3.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Slf4j
@Setter
@Getter
@Component
@Validated
@ConfigurationProperties(prefix = DBProperties.DB)
public class DBProperties {

  public static final String DB = "db";

  @NotBlank private String user;

  @NotBlank private String password;

  @NotBlank private String name;

  @Positive private int port;

  @NotBlank private String host;

}
