package appl.test.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.CookieHandler;
import java.net.CookieManager;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class MonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitoringApplication.class, args);
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
	}

}
