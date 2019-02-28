package com.jake.JavaBatchSpring;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
//NOT_USED - can be used for database connections if needed
public class SpringBatchConfig extends DefaultBatchConfigurer {

	@Override
	public void setDataSource(DataSource ds)
	{
		
	}
}
