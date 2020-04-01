
# Host DNS name doesn't resolve in Docker alpine images

export DOCKER_HOST_IP=$(hostname -I | sed -e 's/ .*//g')
export TERM=dumb

export SDKMAN_DIR=/home/circleci/.sdkman

source "$SDKMAN_DIR/bin/sdkman-init.sh"
