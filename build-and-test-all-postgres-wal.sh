#! /bin/bash

set -e

export DATABASE=postgres
export MODE=wal
export READER=PostgresWalReader
export SPRING_PROFILES_ACTIVE=postgres

./_build-and-test-all.sh
