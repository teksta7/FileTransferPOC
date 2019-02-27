package com.jake.JavaBatchSpring;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemProcessor;
//import javax.inject.Named;

import org.json.JSONObject;
import org.json.XML;

public class ReadLatestFileProcessor implements ItemProcessor{
	
	File localfile;
	File xmlFile;
	List<File> fileList;
	
//	public List<File> processItem(Object file) throws Exception {
//		
//	}

	@Override
	public List<File> process(Object file) throws Exception {
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
		//xmlFile = (File) file;
		//xmlFile = new Fil
		
		FileWriter localWriter = new FileWriter(xmlFile, false);
		localWriter.write(convertedXML);
		localWriter.close();
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO,"TRANSFORMED FILE TO: " + xmlFile.getAbsolutePath());
		//fileList.add(xmlFile);
		fileList.add(localfile);
		return null;
	}

}
