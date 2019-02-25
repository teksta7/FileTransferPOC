package com.jake.javaBatch;

import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Named;

@Named("DownloadFolderReader")
public class DownloadFolderReader extends AbstractItemReader {

	private File[] localDirectory;
	File latestCreatedFile;
	
	@Override
	public void open(Serializable checkpoint) throws Exception 
	{
		localDirectory = new File(System.getProperty("user.dir")).listFiles();
		
	}
	
	
	@Override
	public Object readItem() throws Exception {
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
		return latestCreatedFile;
	}

}
