package bio.overture.backup.postgres2s3.service;

import bio.overture.backup.postgres2s3.properties.BackupProperties;
import com.amazonaws.services.s3.AmazonS3;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BackupService implements Runnable {

  private final S3Service s3Service;
  private final DBDumpService dbDumpService;

  @Autowired
  public BackupService(@NonNull S3Service s3Service,
      @NonNull DBDumpService dbDumpService ) {
    this.s3Service = s3Service;
    this.dbDumpService = dbDumpService;
  }

  @SneakyThrows
  @Scheduled(cron = "${backup.cron-schedule}")
  public void run() {
    log.info("Dumping database....");
    val file = dbDumpService.dumpDb();
    log.info("   - dumped database to {}", file.toString());

    log.info("Uploading file to object storage ....");
    s3Service.uploadFile(file);
    log.info("   - uploaded file {} to object storage", file.toString());
  }

}
