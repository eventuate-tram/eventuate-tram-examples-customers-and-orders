#! /bin/bash

set -e

export DATABASE=postgres
export MODE=wal

export SPRING_PROFILES_ACTIVE=postgres

./_build-and-test-all.sh
