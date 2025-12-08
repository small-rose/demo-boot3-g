package com.small.rose.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;

@SpringBootApplication(exclude = { ThymeleafAutoConfiguration.class})
public class DemoBoot3Application {

	public static void main(String[] args) {
		SpringApplication.run(DemoBoot3Application.class, args);
	}

}
