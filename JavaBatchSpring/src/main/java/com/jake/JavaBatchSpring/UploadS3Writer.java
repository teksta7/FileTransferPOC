package com.jake.JavaBatchSpring;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemWriter;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class UploadS3Writer implements ItemWriter{

//	@Override
//	public void writeItems(List<Object> items) throws Exception {
//		
//		
//	}

	@Override
	public void write(List items) throws Exception {
		// TODO Auto-generated method stub
		try {
			AmazonS3 s3c = AmazonS3ClientBuilder.standard()
					.withRegion(Regions.EU_WEST_2)
					.withCredentials(new ProfileCredentialsProvider())
					.build();
			
			File s3XMLFileToPush = (File) items.get(0);
			Logger.getLogger(ReadLatestFileProcessor.class.getName())
			.log(Level.INFO,"Preparing to upload file to S3...");
			//###################################################
			PutObjectRequest req = new PutObjectRequest("file-transfer-storage-poc", s3XMLFileToPush.getName(), s3XMLFileToPush);
			ObjectMetadata metaD = new ObjectMetadata();
			metaD.setContentType("text/xml");
			metaD.addUserMetadata("Java-Batch-Processing", "Transformed Copy");
			req.setMetadata(metaD);
			//s3c.putObject(req); //ACTIVATE ON FRIDAY
			//###################################################
			//###################################################
			File s3JSONFileToPush = (File) items.get(1);
			Logger.getLogger(ReadLatestFileProcessor.class.getName())
			.log(Level.INFO,"Preparing to upload file to S3...");
			PutObjectRequest Jreq = new PutObjectRequest("file-transfer-storage-poc", s3JSONFileToPush.getName(), s3JSONFileToPush);
			ObjectMetadata JmetaD = new ObjectMetadata();
			metaD.setContentType("application/json");
			metaD.addUserMetadata("Java-Batch-Processing", "Original Copy");
			req.setMetadata(JmetaD);
			//s3c.putObject(Jreq); ACTIVATE ON FRIDAY
			//###################################################
			Logger.getLogger(DownloadFolderReader.class.getName())
			.log(Level.INFO,"FILE UPLOADED");
		}
		catch(AmazonServiceException aws)
		{
			aws.printStackTrace();
		}		
	}
}
