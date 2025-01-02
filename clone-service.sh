#! /bin/bash -e

set -o pipefail

SERVICE=${1?}

mkdir "$SERVICE"

(cd customer-service ; tar cf - $(find . -name build.gradle)) | tar -C "$SERVICE" -xf -

cd "$SERVICE"

rename -S customer-service "$SERVICE" customer-service-*

sd customer-service order-service $(find . -name build.gradle)