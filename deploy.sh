if [ "$1" = "notest" ] ; then
    mvn clean install -Dmaven.test.skip
elif [ "$1" = "test" ] ; then
    mvn clean install
fi

sh $CATALINA_HOME/bin/catalina.sh stop -force
rm -rf $CATALINA_HOME/webapps/pop*
cp target/*.war $CATALINA_HOME/webapps

if [ "$2" = "freshdb" ] ; then
    pushd .
    cd $JAVA_HOME/db/bin
    ./stopNetworkServer &
    rm -rf unit-testing-jpa/
    ./startNetworkServer &
    pushd
fi

sh $CATALINA_HOME/bin/catalina.sh start
