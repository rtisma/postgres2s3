package bio.overture.backup.postgres2s3.config;

import bio.overture.backup.postgres2s3.properties.BackupProperties;
import bio.overture.backup.postgres2s3.util.CommandRunner;
import bio.overture.backup.postgres2s3.util.FileIO;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

import static bio.overture.backup.postgres2s3.util.FileIO.checkExecutableFileExists;

@Configuration
public class AppConfig {

  private final BackupProperties backupProperties;

  @Autowired
  public AppConfig(@NonNull BackupProperties backupProperties) {
    this.backupProperties = backupProperties;
  }

  @Bean
  public CommandRunner pgDumpCommandRunner() throws IOException {
    checkExecutableFileExists(backupProperties.getPgDumpExePath());
    return CommandRunner.builder()
        .command(backupProperties.getPgDumpExePath().toAbsolutePath().toString())
        .build();
  }

}
