#!/bin/sh
nohup sh /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement=0.0.0.0 &>/tmp/storage/WildFly.log &
echo "Started Wildfly"
cd /tmp/JBeretUI
#nohup npm start &>/tmp/storage/JBeretUI.log &
nohup gulp --restUrl http://localhost:8080/java-batch/api/ &> /tmp/storage/JBeretUI.log &
echo "Started JBeretUI, Container started successfully"
exec "/bin/bash"
