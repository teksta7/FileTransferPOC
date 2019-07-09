package com.jake.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.jake.mockServiceDemo.Application;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("INTEGRATION_TEST")
@ContextConfiguration
public class CucumberRoot {
    @Autowired
    protected TestRestTemplate template;
    
    private ResponseEntity<String> response; // output

    @Before
    public void before() {
    	
        // demo to show how to add custom header Globally for the http request in spring test template , like  user header
        template.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders()
                    .add("userHeader", "user");
            return execution.execute(request, body);
        }));
    }
    
    @When("^the client calls /version$")
    public void the_client_issues_GET_health() throws Throwable {
        response =  template.getForEntity("/health", String.class);
    }
    @Then("^the client receives response status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        HttpStatus currentStatusCode = response.getStatusCode();
        assertThat("status code is incorrect : " +
                response.getBody(), currentStatusCode.value(), is(statusCode));
    }

}