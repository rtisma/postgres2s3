package bio.overture.backup.postgres2s3.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Slf4j
@Setter
@Getter
@Component
@Validated
@ConfigurationProperties(prefix = BackupProperties.BACKUP)
public class BackupProperties {

  public static final String BACKUP = "backup";

  @NotBlank private String prefix;

  @NotBlank private String cronSchedule;

}
