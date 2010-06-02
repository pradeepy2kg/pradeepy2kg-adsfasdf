mvn clean install
sh $CATALINA_HOME/bin/catalina.sh stop -force
rm -rf $CATALINA_HOME/webapps/pop*
cp target/*.war $CATALINA_HOME/webapps
sh $CATALINA_HOME/bin/catalina.sh start 
