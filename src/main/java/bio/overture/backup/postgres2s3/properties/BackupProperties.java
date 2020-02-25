package bio.overture.backup.postgres2s3.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.nio.file.Path;

@Slf4j
@Setter
@Getter
@Component
@Validated
@ConfigurationProperties(BackupProperties.BACKUP)
public class BackupProperties {

  public static final String BACKUP = "backup";

  @NotBlank
  @Pattern(regexp = "^[A-Za-z0-9_\\-]+$")
  private String prefix;

  @NotBlank
  @Pattern(regexp = "^((\\*|\\?|\\d+((\\/|\\-){0,1}(\\d+))*)\\s*){6}$")
  private String cronSchedule;

  @NotNull private Path pgDumpExePath;

}
