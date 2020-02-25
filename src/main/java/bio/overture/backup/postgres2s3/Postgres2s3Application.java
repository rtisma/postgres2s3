package bio.overture.backup.postgres2s3;

import bio.overture.backup.postgres2s3.properties.BackupProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Postgres2s3Application {

	public static void main(String[] args) {
		SpringApplication.run(Postgres2s3Application.class, args);
	}

}
