#!/usr/bin/env bash

LOOPS=1000
THREADS=10

REPORT_URL=$(curl -X POST --header "Content-Type: application/json" --header "Accept: */*" "http://${DOCKER_HOST_IP:-localhost}:8084/testplan?loops=${LOOPS}&threads=${THREADS}&rampTime=1&domain=customerservice&port=8080")

echo $REPORT_URL


until curl --fail $REPORT_URL >& /dev/null
do
	echo -n .
	sleep 1
done
echo services are started

