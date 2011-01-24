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
cp /data/code/popreg/target/ecivil.war /data/tomcat6/webapps/ecivil.war
rm -rf /data/tomcat6/work/Catalina/localhost/
rm -rf /data/tomcat6/webapps/ecivil
rm -rf /data/tomcat6/logs/*
rm -rf /data/logs/*

#sleep 2
#echo "Starting Tomcat .."

mkdir /data/tomcat6/webapps/ecivil
cd /data/tomcat6/webapps/ecivil
jar xvf ../ecivil.war
rm ../ecivil.war
cd /data/code/popreg

ant -Dtomcat.home=/data/tomcat6 -Dwebapp.path=/data/tomcat6/webapps/ecivil

#echo " "
#echo "Extract the WAR file to /data/tomcat6/webapps/ecivil"
#echo "ant -Dtomcat.home=/data/tomcat6 -Dwebapp.path=/data/tomcat6/webapps/ecivil"
#echo " "
echo "Backup and clear database and start Tomcat as /data/tomcat6/bin/catalina.sh start"

