#!/usr/bin/env bash


START=$(curl -s localhost:8099/actuator/prometheus | grep -v '#' | grep first | sed -e 's/.* //')
LATEST=$(curl -s localhost:8099/actuator/prometheus | grep -v '#' | grep latest | sed -e 's/.* //')

echo $LATEST - $START | jshell

