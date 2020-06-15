#! /bin/bash

set -e

export DATABASE=mssql
export MODE=polling

export SPRING_PROFILES_ACTIVE=mssql

./_build-and-test-all.sh
