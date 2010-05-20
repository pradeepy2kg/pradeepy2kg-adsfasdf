mvn clean install
rm -rf $CATALINA_HOME/webapps/epopulation*
cp target/*.war $CATALINA_HOME/webapps
