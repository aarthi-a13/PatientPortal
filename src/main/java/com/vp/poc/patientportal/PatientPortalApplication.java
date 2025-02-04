package com.vp.poc.patientportal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PatientPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientPortalApplication.class, args);
	}

	@Bean
	public ChatClient chatClient(ChatClient.Builder builder) {
		return builder.build();
	}

}
