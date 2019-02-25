package com.jake.aws.camel_lambda;

import org.apache.camel.builder.RouteBuilder;

public class AWSCamelRouteBuilder extends RouteBuilder {	
	// .log("We have a message! ${body}")
			//.setHeader("identity").jsonpath("$['type']")
			//.filter(simple("${header.identity} == 'login'"))
	
	@Override
	public void configure() throws Exception {
		LambdaBean LB = new LambdaBean();
		// TODO Auto-generated method stub
		from("aws-sqs://file-transfer-notification?accessKey=AKIAJWNS2ZBBOYT7JN6Q&secretKey=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&maxMessagesPerPoll=1")
		//from("direct:start")	
			.log("Picked message off SQS... Processing")
			.log("Message Body = ${body}")
			.bean(LB,"log")
			//.to("bean:LambdaBean?method=log");
			.to("aws-sqs://file-transfer-poc?accessKey=AKIAJWNS2ZBBOYT7JN6Q&secretKey=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");
			System.out.println("End of camel route");
		
	}
	
	/* SQS LOGIC
	 * public void configure() throws Exception { // TODO Auto-generated method stub
	 * from(
	 * "aws-sqs://file-transfer-notification?accessKey=AKIAJWNS2ZBBOYT7JN6Q&secretKey=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&maxMessagesPerPoll=1")
	 * .log("Picked message off SQS... Processing") .log("Message Body = ${body}")
	 * .to(
	 * "aws-sqs://file-transfer-poc?accessKey=AKIAJWNS2ZBBOYT7JN6Q&secretKey=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"
	 * ); //System.out.println("You are now in a camel route");
	 * 
	 * }
	 */

}
