package com.rabbitmqstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class RabbitmqStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitmqStreamApplication.class, args);
	}

}
