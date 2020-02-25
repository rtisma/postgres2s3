package bio.overture.backup.postgres2s3.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BackupService implements Runnable {

  @Scheduled(cron = "* * * * * *")
  public void run() {
    log.info("hi: {}", System.currentTimeMillis());
  }

}
