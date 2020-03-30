#! /bin/bash -e

export SDKMAN_DIR=/home/circleci/.sdkman
curl -s "https://get.sdkman.io" | bash
source "$SDKMAN_DIR/bin/sdkman-init.sh"
sdk install java 14.0.0-open
