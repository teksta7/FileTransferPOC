package com.jake.javaBatch;

import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobInstance;
import javax.inject.Inject;
import javax.inject.Named;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


@Named("DownloadFolderReader")
public class DownloadFolderReader extends AbstractItemReader {

	private File[] localDirectory;
	File latestCreatedFile;
	AmazonS3 s3c;
	ListObjectsV2Result objectsInBucket;
	//Producer mongoDbConn;
	@Inject
	MongoClient mongoClient;

	
	@Override
	public void open(Serializable checkpoint) throws Exception 
	{
		localDirectory = new File(System.getProperty("user.dir")).listFiles();
		s3c = AmazonS3ClientBuilder.standard()
				.withRegion(Regions.EU_WEST_2)
				.withCredentials(new ProfileCredentialsProvider())
				.build();
		//objectsInBucket = s3c.listObjectsV2("file-transfer-storage-poc");
		
	}
	
	//Check if file scanned on file system already exists in the S3 bucket.
		// if not then the batch process will continue, if so then it will throw an error
		private File s3BucketIfFileExists(File fileToCheck) throws AmazonS3Exception
		{
			objectsInBucket = s3c.listObjectsV2("file-transfer-storage-poc");
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
		// Make a database server connection
		System.out.println("Calling Mongo Db server for connection");
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		//Get the database
		DB db = mongoClient.getDB("mydb");		
        // get a collection object to work with
        DBCollection coll = db.getCollection("FileTransfer");

        // drop all the data in it
        coll.drop();

        // make a document and insert it
        BasicDBObject doc = new BasicDBObject("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("info", new BasicDBObject("x", 203).append("y", 102));

        coll.insert(doc);
		
        // get the data (since it's the only one in there since we dropped the rest earlier on)
        DBObject myDoc = coll.findOne();
        System.out.println(myDoc);
		
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

}