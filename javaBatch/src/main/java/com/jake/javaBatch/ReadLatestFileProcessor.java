package com.jake.javaBatch;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

import org.json.JSONObject;
import org.json.XML;

@Named("ReadLatestFileProcessor")
public class ReadLatestFileProcessor implements ItemProcessor{
	
	File localfile;
	File xmlFile;
	
	public File processItem(Object file) throws Exception {
		// TODO Auto-generated method stub
		localfile = (File) file;
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO, localfile.getName() + " being processed from " + localfile.getAbsolutePath());
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO,"Is Readable...? " + localfile.canRead());
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO,"Full path value: " + Paths.get(localfile.getAbsolutePath().toString()));
		
		JSONObject localJson = new JSONObject(new String(Files.readAllBytes
				(Paths.get(localfile.getAbsolutePath()))));
		String convertedXML = XML.toString(localJson);
		
		xmlFile = new File(localfile.getAbsolutePath().replace(".json", ".xml"));
		
		FileWriter localWriter = new FileWriter(xmlFile, false);
		localWriter.write(convertedXML);
		localWriter.close();
		return xmlFile;
	}

}
