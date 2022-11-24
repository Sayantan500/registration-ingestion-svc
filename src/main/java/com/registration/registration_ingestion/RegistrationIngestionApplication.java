package com.registration.registration_ingestion;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableRabbit
public class RegistrationIngestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegistrationIngestionApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	@Value("${baseurl.signin}")
	private String baseUrlLogin;

	@Bean(name = "login_base_url")
	public String getBaseUrlLogin(){
		return baseUrlLogin;
	}

	@Value("${values.email_name_pattern_regex}")
	private String email_nameBox_regex;

	@Bean(name = "email_name_pattern")
	public String getEmail_nameBox_regex(){
		return email_nameBox_regex;
	}

}
