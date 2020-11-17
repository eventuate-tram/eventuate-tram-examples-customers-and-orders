#! /bin/bash

set -e

export DATABASE=mysql
export MODE=binlog
export READER=MySqlReader

./_build-and-test-all.sh
