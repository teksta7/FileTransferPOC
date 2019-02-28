package com.jake.JavaBatchSpring;

import java.io.File;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

//Custom listener for the Reader bean due to being the most susceptible to errors
public class CustomReaderListener implements ItemReadListener<File>{
	
	@Value("#{jobExecution}")
	public JobExecution JE;
	
	@Override
	public void beforeRead() {
		// TODO Auto-generated method stub
		System.out.println("About to trigger read job: ITEM_READ_LISTENER");
		
	}

	public void afterRead(File item) {
		// TODO Auto-generated method stub
		System.out.println("Finishing Read Job: ITEM_READ_LISTENER");
		
	}

	@Override
	public void onReadError(Exception ex) {
		// TODO Auto-generated method stub
		System.out.println("Error occured, processing..." + ex.getMessage());
		
		
		/*
		 * System.out.println("Status: " + JE.getExitStatus());
		 * JE.setExitStatus(ExitStatus.COMPLETED); JE.setStatus(BatchStatus.COMPLETED);
		 * System.out.println("Telling Batch Runtime to shutdown..."); JE.stop();
		 */
		
		
//		SE.getJobExecution().setExitStatus(ExitStatus.COMPLETED);
//		SE.setStatus(BatchStatus.COMPLETED);
//		SE.setTerminateOnly();
	}

}
