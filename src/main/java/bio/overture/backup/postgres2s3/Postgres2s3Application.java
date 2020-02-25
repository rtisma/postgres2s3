package bio.overture.backup.postgres2s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Postgres2s3Application {

	public static void main(String[] args) {
		SpringApplication.run(Postgres2s3Application.class, args);
	}

}
