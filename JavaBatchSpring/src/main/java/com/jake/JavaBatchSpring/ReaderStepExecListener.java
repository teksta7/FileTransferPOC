package com.jake.JavaBatchSpring;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class ReaderStepExecListener implements StepExecutionListener {

	@Override
	// To execute before step
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("About to trigger read job: STEP_EXEC_LISTEN");
	}

	@Override
	// To execute after step and return convert error for graceful exit out of batch
	public ExitStatus afterStep(StepExecution stepExecution) {
		// Log Exit Code received from Step
		System.out.println("STEP_EXEC_LISTEN: Error occured, processing..."
				+ stepExecution.getExitStatus().getExitCode().toString());
		
		// Override status codes for JavaBatch and Spring Batch to a success exit code
		stepExecution.setExitStatus(ExitStatus.COMPLETED);
		stepExecution.getJobExecution().setExitStatus(ExitStatus.COMPLETED);
		stepExecution.setStatus(BatchStatus.COMPLETED);
		System.out.println("Shutting step execution down...");
		
		// Only needed if you want no code further on to run
		// stepExecution.setTerminateOnly();
		return ExitStatus.COMPLETED;
	}

}