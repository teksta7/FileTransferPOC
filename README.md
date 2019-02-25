Welcome to the File Transfer POC repo (AWS)
==============================================

In this repo are a number of projects and code snippets that form up an async solution to transfer files across the AWS platform

What's Here
-----------

Code includes the following

* README.md - this file
* API Gateway - Swagger file that defines the entrypoint into the solution, can easily be deployed and read by AWS API Gateway along with many other thrid party API managers/Gateways that support the Swagger format
  
* pom.xml - NOT IN USE YET
* Dummy Mock Service - this directory contains a springboot project to bring up a simple microservice that contains some REST endpoints that can be hit for testing environments/network config
* javaBatch- this directory contains a java project to test out Java Batch processing working with AWS services(S3)
* template.yml - this directory contains Lambda function code in a variety of languages including Python, Node.js and Java to help facilitate the file transfer solution, including communication with FTP servers, AWS SNS, AWS SQS and AWS S3

What Do I Do Next?
------------------
You will need an AWS account to try out most of the code in this project.
You may also need to AWS SDK for building and deployment of lambda functions


------------------


What is next for the solution?
------------------

BUILD PIPELINE

Proper Maven config with correct poms

Other AWS Services
