package com.jake.javaBatch;

import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;

public class batchProcessor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello");	
		JobOperator JO = BatchRuntime.getJobOperator();
		Properties props = new Properties();
		props.put("forceFailure", "true");
		//System.out.println(JO.getJobNames());
		JO.start("awsBackup", props);
		//props.setProperty(key, value)
	}

}
