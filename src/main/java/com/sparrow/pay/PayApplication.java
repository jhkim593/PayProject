package com.sparrow.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class PayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayApplication.class, args);
	}

}
