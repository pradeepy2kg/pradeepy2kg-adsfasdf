#!/bin/sh

/data/tomcat6/bin/catalina.sh stop
echo "Stopping Tomcat ..."
sleep 2

hg pull
hg update

mvn clean install -o -Dmaven.test.skip
echo "Updated and built the code.."
sleep 2

echo "Deploying updates .."
rm /data/tomcat6/webapps/popreg.war
cp /data/code/popreg/target/ecivil.war /data/tomcat6/webapps/ecivil.war
rm -rf /data/tomcat6/work/Catalina/localhost/
rm -rf /data/tomcat6/webapps/ecivil
rm -rf /data/tomcat6/webapps/popreg

sleep 2
echo "Starting Tomcat .."
/data/tomcat6/bin/catalina.sh start

