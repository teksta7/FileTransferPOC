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
  To build the docker image from the included docker file you will need: Docker, JBeretUI Source, WildFly 16 and compiled Java Batch project. The following commands are then used
  - sudo docker build --no-cache -t teksta7/java-batch-poc:latest .
  - sudo docker run -t -d -v dockerVM -p 8080:8080 -p 3000:3000 -p 3001:3001 -p 9990:9990 -p 4713:4713 -p 25:25 -p 4712:4712 -p 8443:8443 -p 8009:8009 -p 9993:9993 --name JBPOC teksta7/java-batch-poc
* template.yml - this directory contains Lambda function code in a variety of languages including Python, Node.js and Java to help facilitate the file transfer solution, including communication with FTP servers, AWS SNS, AWS SQS and AWS S3
* AWSBatch - bash file that will run in a docker container within AWS Batch using AWS ECS behind the scenes to run the container. Looks for file with latest last modified date and will check if its present in S3 bucket, if not present, it will push the file to an S3 bucket. If present it will not push the file.

To prepare use the following commands
- sudo docker build -t teksta7/aws-batch-demo:latest . (Replace the local repo name "teksta7/aws-batch-demo" with your own)
- sudo docker tag teksta7/aws-batch-demo teksta7/aws-batch-demo (Replace the first argument with the local repo name used in the command above and the second argument with the remote repo name of where you would like to push the image, usually a docker hub name)
- sudo docker push teksta7/aws-batch-demo (push image to remote repo, docker hub by default)

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
