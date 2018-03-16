#! /bin/bash

set -e

export DATABASE=mysql

./_build-and-test-all.sh
