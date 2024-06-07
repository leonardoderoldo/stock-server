FROM 268578375494.dkr.ecr.us-east-1.amazonaws.com/centos7.9.2009_jre19.0.2

ADD agent-config.yaml $APP_HOME/agent-config.yaml

ADD build/libs/stock-server-0.0.1-SNAPSHOT.jar $APP_HOME/app.jar

ADD docker_startup.sh $APP_HOME/docker_startup.sh
RUN chmod a+x $APP_HOME/docker_startup.sh

CMD ./docker_startup.sh