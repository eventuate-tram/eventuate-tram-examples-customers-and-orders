#! /bin/bash

host=$1
ports=$2

shift 2

count=0

done=false
while [[ "$done" = false ]]; do
	for port in $ports; do
		curl --fail http://${host}:${port}/actuator/health >& /dev/null
		if [[ "$?" -eq "0" ]]; then
			done=true
		else
			done=false
			break
		fi
	done
	if [[ "$done" = true ]]; then
		echo services are started
		break;
  fi
	(( count++ ))
	if [ "${WAIT_FOR_SERVICES_ITERATIONS:-300}" == "$count" ] ; then
		echo Giving up after $count iterations
		exit 99
	fi
	echo -n .
	sleep 1
done
