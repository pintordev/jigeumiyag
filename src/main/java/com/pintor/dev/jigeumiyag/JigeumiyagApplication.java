package com.pintor.dev.jigeumiyag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JigeumiyagApplication {

	public static void main(String[] args) {
		SpringApplication.run(JigeumiyagApplication.class, args);
	}

}
