package com.jake.mockServiceDemo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class MainController {
	@ResponseBody
	@RequestMapping("/test/200") //Make microservice available via REST call
	public ResponseEntity<String> MockResponse(HttpServletRequest request)
	{
		if(request.getRequestURI().contains("200"))
		{
			System.out.println("200 Response triggered");
			return new ResponseEntity<>("Microservice is active", HttpStatus.OK);
		}
		if(request.getRequestURI().contains("400"))
		{
			System.out.println("400 Response triggered");
			return new ResponseEntity<>("Microservice received bad data", HttpStatus.BAD_REQUEST);
		}
		else
		{
			System.out.println("500 Response triggered");
			return new ResponseEntity<>("Microservice went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
