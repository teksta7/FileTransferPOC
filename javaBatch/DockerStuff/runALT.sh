#!/bin/sh
nohup sh /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement=0.0.0.0 &>/tmp/storage/WildFly.log &
echo "Started Wildfly"
sleep 8s
sh /opt/jboss/wildfly/bin/jboss-cli.sh --connect --file=/tmp/storage/commands.cli
cd /tmp/JBeretUI
#nohup npm start &>/tmp/storage/JBeretUI.log &
nohup gulp --restUrl http://localhost:8080/java-batch/api &> /tmp/storage/JBeretUI.log &
echo "Started JBeretUI, Container started successfully"
exec "/bin/bash"
