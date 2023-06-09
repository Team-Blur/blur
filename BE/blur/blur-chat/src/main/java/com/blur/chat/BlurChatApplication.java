package com.blur.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BlurChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlurChatApplication.class, args);
	}

}
