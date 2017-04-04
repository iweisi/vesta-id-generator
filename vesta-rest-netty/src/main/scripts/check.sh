#!/bin/bash

#java设置
export JAVA_HOME=/usr/local/jdk1.6.0_45
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$PATH:/sbin:/bin:/usr/sbin:/usr/bin:/usr/X11R6/bin

BASE_PATH=`cd "$(dirname "$0")"; pwd`
APP_PATH=`cd "$(dirname "$BASE_PATH")"; pwd`
export APP_PATH

sleep 2

sh $BASE_PATH/server.sh check

if [ $? -eq 0 ];then
  exit 0
else
	exit -1
fi
