FROM amazonlinux:latest
RUN yum -y install jq unzip aws-cli cut
ADD AWSBashBatchJob.sh /usr/local/bin/AWSBashBatchJob.sh
RUN ["chmod", "+x", "/usr/local/bin/AWSBashBatchJob.sh"]
USER nobody
RUN mkdir /tmp/storage
WORKDIR /tmp/storage
ADD AWSBashBatchJob /tmp/storage/AWSBashBatchJob
ADD AWSBatchEnvCommand.txt /tmp/storage/AWSBatchEnvCommand.txt
ADD wildfly-batch-DockerFile /tmp/storage/wildfly-batch-DockerFile
ENTRYPOINT ["/usr/local/bin/AWSBashBatchJob.sh"]
