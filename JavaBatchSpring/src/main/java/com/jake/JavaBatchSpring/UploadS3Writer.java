package com.jake.JavaBatchSpring;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemWriter;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@EnableBatchProcessing
public class UploadS3Writer implements ItemWriter{

	File jsonFile; //Json file to write
	File xmlFile; //xml file to write
	String[] paths; //used to hold split string data of the filenames to push
	int count = 0; //For loop counter

	@Override
	public void write(List items) throws Exception {
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
			PutObjectRequest req = new PutObjectRequest("file-transfer-storage-poc", xmlFile.getName(), xmlFile);
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
			PutObjectRequest Jreq = new PutObjectRequest("file-transfer-storage-poc", jsonFile.getName(), jsonFile);
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
	}
}
