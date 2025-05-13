package org.example.cinemapjt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CinemaPjtApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaPjtApplication.class, args);
	}

}
