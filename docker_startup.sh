#!/bin/bash

# turn on bash's job control
set -m

# Start the primary process and put it in the background
java -Xms${APP_XMS}m -Xms${APP_XMX}m -XX:MaxMetaspaceSize=${APP_MAX_METASPACE_SIZE}m -XX:+UseG1GC -Duser.timezone=GMT-3 -jar $APP_HOME/app.jar &

# Start the helper process
$APP_HOME/grafana-agent-linux-amd64 --config.file=$APP_HOME/agent-config.yaml

# the my_helper_process might need to know how to wait on the
# primary process to start before it does its work and returns


# now we bring the primary process back into the foreground
# and leave it there
fg %1
