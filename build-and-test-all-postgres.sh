#! /bin/bash

set -e

export DATABASE=postgres

./_build-and-test-all.sh
