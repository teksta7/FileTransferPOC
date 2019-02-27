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
    return jobBuilders.get("awsBackup")
        .start(awsBackupStep(stepBuilders)).build();
  }

  @Bean
  public Step awsBackupStep(StepBuilderFactory stepBuilders) {
	  
    return stepBuilders.get("uploadToS3")
    		.<Object,Object>chunk(1).reader(Reader())
			.processor(Processor())
			.writer(Writer()).build();
  }

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