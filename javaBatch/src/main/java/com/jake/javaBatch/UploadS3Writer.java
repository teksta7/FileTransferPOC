package com.jake.javaBatch;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Named("UploadS3Writer")
public class UploadS3Writer extends AbstractItemWriter{

	File jsonFile; //Json file to write
	File xmlFile; //xml file to write
	String[] paths; //used to hold split string data of the filenames to push
	int count = 0; //For loop counter

	@Inject
	JobContext jobContext;
	
	@Override
	public void writeItems(List items) throws Exception {
		// TODO Auto-generated method stub
		try {
			//Create S3 Clients
			AmazonS3 s3c = AmazonS3ClientBuilder.standard()
					.withRegion(Regions.EU_WEST_2)
					.withCredentials(new ProfileCredentialsProvider())
					.build();			
			Logger.getLogger(ReadLatestFileProcessor.class.getName())
			.log(Level.INFO,"Preparing to upload xml file to S3...");
			
			//Initialise string array to hold filenames
			//Execute loop to split filename data from list - remove unwanted characters
			paths = new String[2];
			for (String split : items.get(0).toString().replace("[", "").replace("]", "").split("#"))
			{
				System.out.println("Found file at " + split);
				paths[count] = split;
				++count;
			}
			count = 0; //workaround in case batch function doesn't reset counter on rerun.
			
			//Create new files
			xmlFile = new File(paths[0].toString());
			jsonFile = new File(paths[1].toString());
			//###################################################
			// Push XML File
			// Sets bucket, file to push, content type, optional metadata
			//###################################################
			PutObjectRequest req = new PutObjectRequest("abhidesaipublicbucket", xmlFile.getName(), xmlFile);
			ObjectMetadata metaD = new ObjectMetadata();
			metaD.setContentType("text/xml");
			metaD.addUserMetadata("Java-Batch-Processing", "Transformed Copy");
			req.setMetadata(metaD);
			s3c.putObject(req);
			//###################################################
			// Push JSON File
			// Sets bucket, file to push, content type, optional metadata
			//###################################################
			Logger.getLogger(ReadLatestFileProcessor.class.getName())
			.log(Level.INFO,"Preparing to upload json file to S3...");
			PutObjectRequest Jreq = new PutObjectRequest("abhidesaipublicbucket", jsonFile.getName(), jsonFile);
			ObjectMetadata JmetaD = new ObjectMetadata();
			JmetaD.setContentType("application/json");
			JmetaD.addUserMetadata("Java-Batch-Processing", "Original Copy");
			Jreq.setMetadata(JmetaD);
			s3c.putObject(Jreq);
			//###################################################
			Logger.getLogger(DownloadFolderReader.class.getName())
			.log(Level.INFO,"FILE UPLOADED");
			s3c.shutdown();
			//s3b.shutdown();

		}
		catch(AmazonServiceException aws)
		{
			aws.printStackTrace();
		}

		logEndBatch();

	}

	private void logEndBatch() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		Table table = dynamoDB.getTable("batch");

		int batchId = (Integer) jobContext.getTransientUserData();

		UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("batchid", Integer.toString(batchId))
				.withUpdateExpression("set batchstatus = :val")
				.withValueMap(new ValueMap()
						.withString(":val", "COMPLETED"));

		try {
			System.out.println("Marking item complete with batchId: " + batchId);
			UpdateItemOutcome outcome = table.updateItem(updateItemSpec);

			System.out.println("PutItem succeeded:\n" + outcome.getUpdateItemResult());
		} catch (Exception e) {
			System.err.println("Unable to update item with batchId: " + batchId);
			System.err.println(e.getMessage());
		}
	}

}
