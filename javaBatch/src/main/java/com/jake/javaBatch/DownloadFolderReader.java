package com.jake.javaBatch;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobInstance;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ListObjectsV2Result;

@Named("DownloadFolderReader")
public class DownloadFolderReader extends AbstractItemReader {

	private File[] localDirectory;
	File latestCreatedFile;
	AmazonS3 s3c;
	ListObjectsV2Result objectsInBucket;

	@Inject
	JobContext jobContext;
	
	@Override
	public void open(Serializable checkpoint) throws Exception 
	{
		localDirectory = new File("/Users/abhishekdesai/Documents/temp").listFiles();
		s3c = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.EU_WEST_2)
				.withCredentials(new ProfileCredentialsProvider())
				.build();
		//objectsInBucket = s3c.listObjectsV2("abhidesaipublicbucket");
		
	}
	
	//Check if file scanned on file system already exists in the S3 bucket.
		// if not then the batch process will continue, if so then it will throw an error
		private File s3BucketIfFileExists(File fileToCheck) throws AmazonS3Exception
		{
			objectsInBucket = s3c.listObjectsV2("abhidesaipublicbucket");
			Logger.getLogger(DownloadFolderReader.class.getName())
			.log(Level.INFO, "Looking for: " + fileToCheck.getName());
			
			for(int i = 0; i<objectsInBucket.getKeyCount(); i++)
			{
					if(objectsInBucket.getObjectSummaries().get(i).getKey().toString().equals(fileToCheck.getName()))
					{
						Logger.getLogger(DownloadFolderReader.class.getName())
						.log(Level.INFO, "Found: " + objectsInBucket.getObjectSummaries().get(i).getKey() + 
						" on S3Bucket, file is already backed up,returning null and exiting...");
						//this.je.stop(); //WOrkaround to stop job
						//Attempts to set the batch status from within the bean to signal an exit
						//this.je.setExitStatus(ExitStatus.NOOP); 
						return null; //Alternate return option
						//break;
					}
					else
					{
						Logger.getLogger(DownloadFolderReader.class.getName())
						.log(Level.INFO, objectsInBucket.getObjectSummaries().get(i).getKey() + 
						" doesn't match current object, continuing to look...");
					}
				}
			
			Logger.getLogger(DownloadFolderReader.class.getName())
			.log(Level.INFO,"No match found on bucket, Preparing file for further processing and transfer");
			return fileToCheck;
		}
	
	
	@Override
	public Object readItem() throws Exception {

		logStartBatch();

		//Read local directory to find file with latest last modified data
				for (File file : localDirectory)
				{
					if (!file.isDirectory() && latestCreatedFile == null)
					{
						latestCreatedFile = file;
					}
					else if (file.lastModified() > latestCreatedFile.lastModified())
					{
						latestCreatedFile = file;
					}
					else
					{
						Logger.getLogger(DownloadFolderReader.class.getName())
							.log(Level.INFO, file.getName() + " is not the latest file, skipping...");
					}
				}
				//###############################################
				//Logger.getLogger(DownloadFolderReader.class.getName())
				//.log(Level.INFO, "S3 check... " + s3BucketIfFileExists(latestCreatedFile).toString());
				Logger.getLogger(DownloadFolderReader.class.getName())
				.log(Level.INFO, "S3 check... ");
				return s3BucketIfFileExists(latestCreatedFile);
				//return null;
	}

	private static int COUNT = 0;

	private void logStartBatch() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("batch");

		int batchId = COUNT++;

		try {
			System.out.println("Adding a new item with batchId: " + batchId);
			PutItemOutcome outcome = table.putItem(new Item()
					.withPrimaryKey("batchid", Integer.toString(batchId))
					.withString("batchstatus", "STARTED"));
			System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
		} catch (Exception e) {
			System.err.println("Unable to add item with batchId: " + batchId);
			System.err.println(e.getMessage());
		}

		jobContext.setTransientUserData(batchId);

	}

}
