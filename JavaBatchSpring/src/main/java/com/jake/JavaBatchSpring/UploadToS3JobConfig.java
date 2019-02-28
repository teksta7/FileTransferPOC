package com.jake.JavaBatchSpring;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class UploadToS3JobConfig {

  @Bean
  public Job uploadToS3Job(JobBuilderFactory jobBuilders,
      StepBuilderFactory stepBuilders) {
	  //Setup job by calling step with JobExecution Listener
	  //- using start instead of flow restricts use of listeners
	  return jobBuilders.get("awsBackup")
			  .preventRestart()
			  .listener(new ReaderExecListener())
			  .flow(awsBackupStep(stepBuilders))
			  .end()
			  .build();
  }

  @Bean
  public Step awsBackupStep(StepBuilderFactory stepBuilders) {
	 // Trigger step and its relevent reader,processor,writer and any listeners each component needs 
    return stepBuilders.get("uploadToS3").listener(new ReaderStepExecListener())
    		.<Object,Object>chunk(1).reader(Reader()).listener(new CustomReaderListener())
			.processor(Processor())
			.writer(Writer()).build(); 
  }

  //Batch Components
  @Bean
  public DownloadFolderReader Reader() 
  {
    return new DownloadFolderReader();
  }

  @Bean
  public ReadLatestFileProcessor Processor() 
  {
    return new ReadLatestFileProcessor();
  }

  @Bean
  public UploadS3Writer Writer() 
  {
    return new UploadS3Writer();
  }
}