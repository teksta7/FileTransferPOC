package com.jake.mockServiceDemo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@EnableConfigurationProperties
@RestController
@ControllerAdvice
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping("/test/200/OK") //Make microservice available via REST call
	public String testOK()
	{
			System.out.println("200 Response triggered");
			return "{\"Status\": \"Microservice is active\"}";
	}
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping("/test/400/OK") //Make microservice available via REST call
	public String testInvalid()
	{
			System.out.println("400 Response triggered");
			return "{\"Status\": \"Microservice got bad data from client\"}";
	}
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping("/test/500/OK") //Make microservice available via REST call
	public String testServer_Error()
	{
			System.out.println("500 Response triggered");
			return "{\"Status\": \"Microservice went wrong\"}";
	}

}
