package com.jake.aws.camel_lambda;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
//import org.apache.camel.Component;
//import org.apache.camel.Main;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.util.jndi.JndiContext;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.jake.aws.camel_lambda.AWSCamelRouteBuilder;

public class LambdaFunctionHandler implements RequestHandler<SNSEvent, String> {

    @Override
    public String handleRequest(SNSEvent event, Context context) {
    	
        context.getLogger().log("Received event: " + event);
        String message = event.getRecords().get(0).getSNS().getMessage();
        context.getLogger().log("From SNS: " + message);
        
        //Create new registry to bind AWS SQS component to
    	//JndiRegistry registry = new JndiRegistry();
    	//Adding Amazon SQS client to Camel Registry to prepare to access queue
    	//AWSCredentials camelCreds = new BasicAWSCredentials("NONE", "NONE");
    	//@SuppressWarnings("deprecation")
    	//AmazonSQS SQSclient = new AmazonSQSClient(camelCreds);
    	//registry.bind("client", SQSclient);
    	//ClientConfiguration camelClientConfig = new ClientConfiguration();
    	//camelClientConfig.setProxyHost("MY_HOSTNAME");
    	//camelClientConfig.setProxyPort("MY_PORT");
        
        //Camel Call
        try {
        	//Amazon S3 Client Setup
        	//JndiRegistry reg = new JndiRegistry();
        	final String accessKey = "";
        	final String secretKey = "";
        	@SuppressWarnings("deprecation")
			AmazonS3 s3c = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
        	//reg.bind("amazonS3Client", s3c);
        	
        	//Java bean setup
	        JndiContext jndiContext = new JndiContext();
	        jndiContext.bind("LambdaBean", new LambdaBean());
	        jndiContext.bind("amazonS3Client", s3c);
	        AWSCamelRouteBuilder aws = new AWSCamelRouteBuilder();
	        CamelContext ctx = new DefaultCamelContext(jndiContext);
	        
	        ProducerTemplate template = ctx.createProducerTemplate();
	        
        	ctx.addRoutes(aws);
        	//Map<String, String> globalParams = new HashMap<String, String>();
        	//globalParams.put("FILENAME", "abc");
			//ctx.setGlobalOptions(globalParams);
        	//ctx.addComponent("event", (Component) event);
        	ctx.start();
        	//template.sendBody("direct:exampleName", message);
        	template.sendBody("direct:start", "Welcome to Camel");
        	System.out.println(jndiContext.lookup("LambdaBean"));
        	//Thread.sleep(5000);
        	
        }
        catch (Exception e) {
			// TODO Auto-generated catch block
			context.getLogger().log("Error starting Camel runtime due to:");
			e.printStackTrace();
		}
        
        /*Main AWSRuntime = new Main();
        AWSRuntime.addRouteBuilder(new AWSCamelRouteBuilder());
        try {
			AWSRuntime.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			context.getLogger().log("Error starting Camel runtime due to:");
			e.printStackTrace();
		} */
        return message;
    }
}
