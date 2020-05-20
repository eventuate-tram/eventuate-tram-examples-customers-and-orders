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

mkdir -p ~/container-logs

docker ps -a > ~/container-logs/containers.txt

for name in $(docker ps -a --format "{{.Names}}") ; do
  docker logs $name > ~/container-logs/${name}.log
done
