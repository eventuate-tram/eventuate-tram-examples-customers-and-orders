#! /bin/bash -e

CONTAINER_IDS=$(docker ps -a -q)

for id in $CONTAINER_IDS ; do
  echo "\n--------------------"
  echo "logs of:\n"
  docker ps -a -f "id=$id" 	
  echo "\n"
  docker logs $id
  echo "--------------------\n"
done

