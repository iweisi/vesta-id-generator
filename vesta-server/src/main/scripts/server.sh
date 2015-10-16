#!/bin/bash

name="Vesta RPC Server"

package=vesta-server-0.0.1.jar
 
#全局变量
BASE_PATH=${BASE_PATH:-`cd "$(dirname "$0")"; pwd`}
APP_PATH=${APP_PATH:-`dirname "$BASE_PATH"`}


#设置java运行参数
GC_OPTS=" -server -Xms512m -Xmx512m -Xmn128m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=60"
GC_LOG_OPTS=" -verbose:gc  -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution  -XX:+PrintGCDetails -Xloggc:./logs/gc.log"
PRG_OPTS=""

#应用客户化变量
if [ -f $APP_PATH/conf/custom.properties ]
then
	source $APP_PATH/conf/custom.properties
fi

JAVA_OPTS="$GC_OPTS $GC_LOG_OPTS $CUST_OPT $PRG_OPTS"


#Java变量
CLASS_PATH=${CLASS_PATH:-$APP_PATH/extlib}
MAIN_CLASS=${MAIN_CLASS:-"./lib/$package"}


exist(){
			if test $( pgrep -f "$MAIN_CLASS" | wc -l ) -eq 0 
			then
				return 1
			else
				return 0
			fi
}

start(){
		
		echo "apppath: $APP_PATH"
		if exist; then
				echo "$name is already running."
				exit 1
		else
	    	cd $APP_PATH
			nohup java $JAVA_OPTS -cp $CLASS_PATH -jar $MAIN_CLASS 2> /dev/null & 
			echo "$name is started."
		fi
}

stop(){
		runningPID=`pgrep -f "$MAIN_CLASS"`
		if [ "$runningPID" ]; then
				echo "$name pid: $runningPID"
        count=0
        kwait=5
        echo "$name is stopping, please wait..."
        kill -15 $runningPID
					until [ `ps --pid $runningPID 2> /dev/null | grep -c $runningPID 2> /dev/null` -eq '0' ] || [ $count -gt $kwait ]
		        do
		            sleep 1
		            count=$count+1;
		        done

	        if [ $count -gt $kwait ]; then
	            kill -9 $runningPID
	        fi
        clear
        echo "$name is stopped."
    else
    		echo "$name has not been started."
    fi
}

check(){
   if exist; then
   	 echo "$name is alive."
   	 exit 0
   else
   	 echo "$name is dead."
   	 exit -1
   fi
}

restart(){
        stop
        start
}

case "$1" in

start)
        start
;;
stop)
        stop
;;
restart)
        restart
;;
check)
        check
;;
*)
        echo "available operations: [start|stop|restart|check]"
        exit 1
;;
esac