package com.jake.JavaBatchSpring;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class ReaderExecListener implements JobExecutionListener{

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		System.out.println("About to trigger read job");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub
		//Final method in batch execution after error from job
		//Extra config to be added to look at the type of error generated(Contained within jobExecution)
		System.out.println("JOB_EXEC_HANDLER: Error occured, processing..." + jobExecution.getExitStatus().toString());
		jobExecution.getExecutionContext().clearDirtyFlag();
		jobExecution.setExitStatus(ExitStatus.COMPLETED); //Spring friendly setting
		jobExecution.setStatus(BatchStatus.COMPLETED); //java batch friendly setting
		System.out.println("Telling Batch Runtime to shutdown with exit status... " + jobExecution.getExitStatus().getExitCode().toString());
		//jobExecution.stop(); //Only needed to prevent multi step jobs from triggering next phase
	}

}
