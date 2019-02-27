package com.jake.JavaBatchSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration;

//AWS Bean fix and stops string needing local database on filesystem, In memory used instead
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, ContextRegionProviderAutoConfiguration.class})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JavaBatchSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaBatchSpringApplication.class, args);
	}

}
