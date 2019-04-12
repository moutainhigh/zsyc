#!/bin/bash
# by lcs
# 2019-01-12

function mvnw() {
	mvn -Dmaven.test.skip=true "$@"
}

function clean() {
	mvn clean
}

function service() {
	mvnw -pl zsyc-service package -am && java -jar zsyc-service/target/*.jar $@
}

function oauth() {
	mvnw -pl zsyc-oauth-service package -am && java -jar zsyc-oauth-service/target/*.jar $@
}

function api() {
	mvnw -pl zsyc-api clean package -am && java -jar zsyc-api/target/*.jar $@
}

function package() {
	mvnw clean package
}

function code() {
	mvnw -pl zsyc-code-generator compile exec:java@CodeGenerator
}

function start() {
	stop
	package
	nohup java -jar zsyc-service/target/*.jar $@ > stdout.log 2>&1 &
	nohup java -jar zsyc-oauth-service/target/*.jar $@ >> stdout.log 2>&1 &
	nohup java -jar zsyc-api/target/*.jar >> stdout.log $@ 2>&1 &
	tail -f stdout.log
}

function stop(){
	PIDS=`ps -ef| grep "java.*\-jar.*target/zsyc\-.*jar" | awk '{print $2}'`
	for PID in $PIDS ; do
		kill_pid $PID
	done
}

function kill_pid() {
    COUNT=0
    PID=$1
    PID_EXIST=`ps -f -p ${PID} | grep java`
    echo "killing $PID ==> $PID_EXIST"
    kill ${PID}
	while [[ ${COUNT} -lt 10 ]]; do
	    PID_EXIST=`ps -f -p ${PID} | grep java`
	    if [[ -n "$PID_EXIST" ]]; then
	        echo ""
            return
        fi
        echo -e ".\c"
        sleep 1
        COUNT=$((COUNT+1))
	done
	echo "\n kill -9 $PID"
	kill -9 ${PID}

}

cd `dirname $0`

command=$1
shift 1

if [ -z "$command" ]; then
	echo "No commands provided. Defaulting to [run]"
	command="run"
fi

case "$command" in
"start")
	start "$@"
	;;
"stop")
	stop
	;;
"service")
	service "$@"
	;;
"api")
	api "$@"
	;;
"oauth")
	oauth "$@"
	;;
"code")
	code "$@"
	;;
"package"|"build")
	package "$@"
	;;
esac
