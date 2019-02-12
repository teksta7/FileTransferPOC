package com.amazonaws.lambda.filetransfer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.jcraft.jsch.*;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {
	
	public void s3Fetch(String keyname)
	{
		 final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
	        try {
	            S3Object o = s3.getObject("file-transfer-storage-poc", keyname);
	            S3ObjectInputStream s3is = o.getObjectContent();
	            FileOutputStream fos = new FileOutputStream(new File("/tmp/" + keyname));
	            byte[] read_buf = new byte[1024];
	            int read_len = 0;
	            while ((read_len = s3is.read(read_buf)) > 0) {
	                fos.write(read_buf, 0, read_len);
	            }
	            s3is.close();
	            fos.close();
	        } catch (AmazonServiceException e) {
	            System.err.println(e.getErrorMessage());
	            System.exit(1);
	        } catch (FileNotFoundException e) {
	            System.err.println(e.getMessage());
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println(e.getMessage());
	            System.exit(1);
	        }
	}

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input.toString());
        s3Fetch(input.toString());
       
        //################################################################
        // EFS Transfer logic
        //################################################################
        context.getLogger().log("Attempting to send file to EFS");
        s3Fetch("Windows.pem");
        //String localfilepath = "/tmp/";
        //String remotefilepath = "/efsDEMO";
        String user = "ec2-user";
        String host = "ec2-18-130-224-86.eu-west-2.compute.amazonaws.com";
        String keyFilePath = "/tmp/Windows.pem";
        try
        {
        	JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            jsch.addIdentity(keyFilePath);
            session.setConfig("StrictHostKeyChecking", "no"); //Lambda POC workaround
            session.connect();
            context.getLogger().log("Connection Successful");
            Channel channel = session.openChannel("sftp");
            channel.setInputStream(System.in);
            channel.setOutputStream(System.out);
            channel.connect();
            
            ChannelSftp FT = (ChannelSftp) channel;
            FT.put("/tmp/" + input.toString(),"/home/ec2-user/" + System.getenv("REMOTE_PATH"));
            FT.exit();   
        }
        catch (Exception e) {
			// TODO: handle exception
        	context.getLogger().log("Error occured: " + e);
        	System.exit(1);
		}
       
        return "Successfully put file into EFS from S3!";
    }

}
