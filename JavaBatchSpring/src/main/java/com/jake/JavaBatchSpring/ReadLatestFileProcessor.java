package com.jake.JavaBatchSpring;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemProcessor;

import org.json.JSONObject;
import org.json.XML;

@EnableBatchProcessing
public class ReadLatestFileProcessor implements ItemProcessor{
	
	File localfile; //jsonFile to process
	File xmlFile; //Transformed XML file;
	List <String> fileList; //Value passed to Writer - HAS TO BE A LIST
	String fileInfo; // Combined info to put into list;

	@Override
	public List<String> process(Object file) throws Exception {
		//Initialise Variables
		fileList = new ArrayList<String>();
		localfile = (File) file;
		xmlFile = (File) file;
		
		//Testing if required data is passed ok from reader
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO, localfile.getName() + " being processed from " + localfile.getAbsolutePath());
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO,"Is Readable...? " + localfile.canRead());
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO,"Full path value: " + Paths.get(localfile.getAbsolutePath().toString()));
		
		//Transform JSON file to XML (EXTRA LOGIC TO BE PUT IN PLACE TO CHECK IF .json)
		JSONObject localJson = new JSONObject(new String(Files.readAllBytes
				(Paths.get(localfile.getAbsolutePath()))));
		String convertedXML = XML.toString(localJson);
		
		//Create XML file and write converted data
		xmlFile = new File(localfile.getAbsolutePath().replace(".json", ".xml"));
		FileWriter localWriter = new FileWriter(xmlFile, false);
		localWriter.write(convertedXML);
		localWriter.close();
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO,"TRANSFORMED FILE TO: " + xmlFile.getAbsolutePath());
		
		//Prepare data for Writer
		String xmlpath = xmlFile.getAbsolutePath();
		String jsonpath = localfile.getAbsolutePath();
		fileInfo = xmlpath + "#" + jsonpath;
		Logger.getLogger(ReadLatestFileProcessor.class.getName())
		.log(Level.INFO,"FILEINFO: " + fileInfo);
		fileList.add(0,fileInfo);
		return fileList;
	}

}
