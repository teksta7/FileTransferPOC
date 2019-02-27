package com.jake.JavaBatchSpring;

import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemReader;
//import javax.batch.operations.JobOperator;
//import javax.batch.runtime.BatchRuntime;
//import javax.batch.runtime.JobInstance;
//import javax.inject.Named;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class DownloadFolderReader implements ItemReader {

	private File[] localDirectory = new File(System.getProperty("user.dir")).listFiles();
	File latestCreatedFile;
	AmazonS3 s3c = AmazonS3ClientBuilder.standard()
			.withRegion(Regions.EU_WEST_2)
			.withCredentials(new ProfileCredentialsProvider())
			.build();
	ListObjectsV2Result objectsInBucket = s3c.listObjectsV2("file-transfer-storage-poc");
	
	private File s3BucketIfFileExists(File fileToCheck) throws AmazonS3Exception
	{
		Logger.getLogger(DownloadFolderReader.class.getName())
		.log(Level.INFO, "Looking for: " + fileToCheck.getName());
		
		for(int i = 0; i<objectsInBucket.getKeyCount(); i++)
		{
				if(objectsInBucket.getObjectSummaries().get(i).getKey().toString().equals(fileToCheck.getName()))
				{
					Logger.getLogger(DownloadFolderReader.class.getName())
					.log(Level.INFO, "Found: " + objectsInBucket.getObjectSummaries().get(i).getKey() + 
					" on S3Bucket, file is already backed up,returning null and exiting...");
					//fileToCheck = null;
					return null;
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
	
	
//	@Override
//	public Object readItem() throws Exception {
//		
//	}

	@Override
	public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
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
		Logger.getLogger(DownloadFolderReader.class.getName())
		.log(Level.INFO, "Returning: " + s3BucketIfFileExists(latestCreatedFile).toString());
		//return s3BucketIfFileExists(latestCreatedFile);
		return null;
	}

}
