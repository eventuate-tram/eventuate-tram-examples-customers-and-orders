#!/usr/bin/env bash


START=$(curl -s ${DOCKER_HOST_IP:-localhost}:8099/actuator/prometheus | grep -v '#' | grep first | sed -e 's/.* //')
LATEST=$(curl -s ${DOCKER_HOST_IP:-localhost}:8099/actuator/prometheus | grep -v '#' | grep latest | sed -e 's/.* //')

echo $LATEST - $START | jshell

