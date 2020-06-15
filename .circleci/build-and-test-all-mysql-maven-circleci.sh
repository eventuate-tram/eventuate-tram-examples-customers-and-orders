#! /bin/bash

set -e

java -version

source ./.circleci/install-jdk-14.sh

java -version

./build-and-test-all-mysql-maven.sh
