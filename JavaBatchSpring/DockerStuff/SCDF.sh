#!/bin/sh
nohup java -jar /opt/spring-cloud-dataflow/spring-cloud-skipper-server-2.0.2.RELEASE.jar &> /opt/log/spring-cloud-skipper.log &
sleep 10s
echo "Started Spring Cloud Skipper Server"
nohup java -jar /opt/spring-cloud-dataflow/spring-cloud-dataflow-server-2.1.0.RELEASE.jar &> /opt/log/spring-cloud-dataflow.log &
echo "Started Spring Cloud DataFlow Server, showing server logs..."
tail -f /opt/log/spring-cloud-dataflow.log
#exec "/bin/bash"
