#Spring Cloud Data Flow Container using Amazon Corretto
#=======================================================================================#
#OS and Core dependency setup
#---------------------------------------------------------------------------------------#
FROM amazonlinux:latest
MAINTAINER teksta7
RUN yum upgrade -y
RUN yum install -y https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
RUN yum install -y wget yum-utils https://centos7.iuscommunity.org/ius-release.rpm
RUN yum install -y curl
RUN mkdir -p /opt/log /tmp/storage /opt/spring-cloud-dataflow
#---------------------------------------------------------------------------------------#
#Amazon Corretto Setup
#---------------------------------------------------------------------------------------#
RUN cd /tmp/storage
WORKDIR /tmp/storage
RUN pwd
RUN wget --no-cookies --content-disposition https://d3pxv6yz143wms.cloudfront.net/11.0.3.7.1/java-11-amazon-corretto-devel-11.0.3.7-1.x86_64.rpm
RUN yum localinstall -y java-11-amazon-corretto-devel-11.0.3.7-1.x86_64.rpm
RUN java -version
#---------------------------------------------------------------------------------------#
#Spring Cloud Data Flow setup
#---------------------------------------------------------------------------------------#
WORKDIR /opt/spring-cloud-dataflow
RUN wget --no-cookies --content-disposition https://repo.spring.io/release/org/springframework/cloud/spring-cloud-dataflow-server/2.1.0.RELEASE/spring-cloud-dataflow-server-2.1.0.RELEASE.jar
RUN wget --no-cookies --content-disposition https://repo.spring.io/release/org/springframework/cloud/spring-cloud-dataflow-shell/2.1.0.RELEASE/spring-cloud-dataflow-shell-2.1.0.RELEASE.jar
RUN wget --no-cookies --content-disposition https://repo.spring.io/release/org/springframework/cloud/spring-cloud-skipper-server/2.0.2.RELEASE/spring-cloud-skipper-server-2.0.2.RELEASE.jar
#---------------------------------------------------------------------------------------#
#Logging, network and config setup
#---------------------------------------------------------------------------------------#
RUN chown +777 /opt/spring-cloud-dataflow/spring-cloud-skipper-server-2.0.2.RELEASE.jar
RUN chmod +x /opt/spring-cloud-dataflow/spring-cloud-skipper-server-2.0.2.RELEASE.jar
RUN chown +777 /opt/spring-cloud-dataflow/spring-cloud-dataflow-server-2.1.0.RELEASE.jar
RUN chmod +x /opt/spring-cloud-dataflow/spring-cloud-dataflow-server-2.1.0.RELEASE.jar
RUN touch /opt/log/spring-cloud-skipper.log
RUN touch /opt/log/spring-cloud-dataflow.log
ENV JAVA_HOME /usr/lib/jvm/java-11-amazon-corretto
RUN yum clean all
RUN rm -rf /var/cache/yum
#---------------------------------------------------------------------------------------#
#Copy Startup script
#---------------------------------------------------------------------------------------#
ADD SCDF.sh /opt/SCDF.sh
RUN chown +777 /opt/SCDF.sh
RUN chmod +x /opt/SCDF.sh
RUN pwd
RUN ls -la /opt/spring-cloud-dataflow
CMD /opt/SCDF.sh
#---------------------------------------------------------------------------------------#
