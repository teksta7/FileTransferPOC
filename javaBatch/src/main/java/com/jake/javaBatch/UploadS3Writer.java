package com.jake.javaBatch;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


@Named("UploadS3Writer")
public class UploadS3Writer extends AbstractItemWriter{

	@Override
	public void writeItems(List<Object> items) throws Exception {
		// TODO Auto-generated method stub
		try {
			AmazonS3 s3c = AmazonS3ClientBuilder.standard()
					.withRegion(Regions.EU_WEST_2)
					.withCredentials(new ProfileCredentialsProvider())
					.build();
			
			File s3FileToPush = (File) items.get(0);
			Logger.getLogger(ReadLatestFileProcessor.class.getName())
			.log(Level.INFO,"Preparing to upload file to S3...");
			PutObjectRequest req = new PutObjectRequest("file-transfer-storage-poc", s3FileToPush.getName(), s3FileToPush.getAbsolutePath());
			ObjectMetadata metaD = new ObjectMetadata();
			metaD.setContentType("plain/text");
			metaD.addUserMetadata("Java-Batch-Processing", "Successful");
			req.setMetadata(metaD);
			s3c.putObject(req);
		}
		catch(AmazonServiceException aws)
		{
			aws.printStackTrace();
		}
		
	}

}
