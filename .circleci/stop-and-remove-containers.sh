#! /bin/bash -e

CONTAINER_IDS=$(docker ps -a -q)

for id in $CONTAINER_IDS ; do
  docker stop $id
  docker rm $id
done

