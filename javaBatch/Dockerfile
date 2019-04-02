#OS and Dependency setup with Mongo DB
FROM amazonlinux:latest
MAINTAINER teksta7
RUN yum upgrade -y
RUN yum install -y https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
RUN yum install -y wget yum-utils https://centos7.iuscommunity.org/ius-release.rpm
RUN cd /tmp
RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/11.0.2+9/f51449fcd52f4d52b93a989c5c56ed3c/jdk-11.0.2_linux-x64_bin.rpm"
RUN yum localinstall -y jdk-11.0.2_linux-x64_bin.rpm 
RUN yum install -y nodejs.x86_64 npm curl make gcc-c++ awscli python36u python36u-pip
RUN yum clean all
RUN rm -rf /var/cache/yum
ADD run.sh /tmp/run.sh
RUN chown +777 /tmp/run.sh
RUN chmod +x /tmp/run.sh
RUN mkdir -p /data/db 

#AWS Creds Config
#ARG AWS_ACCESS_KEY_ID=NOT_FOUND
#ARG AWS_SECRET_KEY=NOT_FOUND
#RUN export DAWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
#RUN export DAWS_SECRET_ACCESS_KEY=$AWS_SECRET_KEY
#RUN export DAWS_DEFAULT_REGION=eu-west-2
#ENV AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
#ENV AWS_SECRET_ACCESS_KEY=$AWS_SECRET_KEY
#ARG AWS_KEY=''
#ARG AWS_SECRET_KEY=''
#ARG AWS_REGION='eu-west-2'
#RUN aws configure set aws_access_key_id $AWS_KEY \
#&& aws configure set aws_secret_access_key $AWS_SECRET_KEY \
#&& aws configure set default.region $AWS_REGION

#WildFly Setup and Java Batch Logic
RUN mkdir /tmp/WildFly /tmp/JBeretUI /tmp/storage /tmp/storage/test
#COPY javaBatch/target/java-batch.war /tmp/WildFly/standalone/deployments/
ADD sampleLarge.json /tmp/storage/test/sampleLarge.json
ADD sampleOLD.json /tmp/storage/test/sampleOLD.json
RUN rm jdk-11.0.2_linux-x64_bin.rpm
COPY wildfly-16.0.0.Final/ /tmp/WildFly/
RUN cd /tmp/WildFly
WORKDIR /tmp/WildFly
ENV JBOSS_HOME /tmp/WildFly
ENV LAUNCH_JBOSS_IN_BACKGROUND true
RUN chown -R +777 /tmp/WildFly
RUN ./bin/add-user.sh pocUser password
RUN touch /tmp/storage/WildFly.log
EXPOSE 9990 3000 3001 8080 8009 8443 4712 4713 25 9993 27017

#JBeret UI Setup
COPY jberet-ui-master/ /tmp/JBeretUI/
WORKDIR /tmp/JBeretUI
RUN npm install -g bower forever
RUN npm install --save gulp-install
RUN npm install
RUN touch /tmp/storage/JBeretUI.log
RUN touch /tmp/storage/MongoDB.log
#CMD ["nohup gulp --restUrl http://localhost:8080/java-batch/api"]
WORKDIR /tmp/storage/test
CMD /tmp/run.sh 


