#! /bin/bash -e

docker run $* \
   --name postgresterm \
   -e POSTGRES_HOST=$DOCKER_HOST_IP \
   --rm postgres:9.6.5 \
   sh -c 'export PGPASSWORD=eventuate; exec psql -h "$POSTGRES_HOST" -U eventuate'
