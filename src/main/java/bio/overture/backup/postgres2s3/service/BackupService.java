package bio.overture.backup.postgres2s3.service;

import bio.overture.backup.postgres2s3.properties.BackupProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BackupService implements Runnable {

  @Scheduled(cron = "${backup.cron-schedule}")
  public void run() {
    log.info("hi: {}", System.currentTimeMillis());
  }

}
