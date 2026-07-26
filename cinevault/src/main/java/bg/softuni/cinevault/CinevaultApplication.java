package bg.softuni.cinevault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CinevaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinevaultApplication.class, args);
	}

}
