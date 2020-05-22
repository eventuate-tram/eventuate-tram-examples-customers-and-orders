FROM openjdk:8u252-slim-buster

RUN apt-get update && \
    apt-get install -y curl && \
    apt-get install -y jq && \
    apt-get install -y \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg2 \
        software-properties-common && \
  ( curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - ) && \
  add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/debian \
   $(lsb_release -cs) \
   stable" && \
  apt-get update && \
  apt-get install -y docker-ce-cli && \
  curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
  chmod +x /usr/local/bin/docker-compose && \
  rm -rf /var/lib/apt/lists/*

WORKDIR /eventuate-tram-examples-customers-and-orders
CMD echo Ready ; sleep 260000

COPY build /tmp-src

RUN echo "Building in image..." && (cd /tmp-src ; ./gradlew testClasses assemble ; cd ; rm -fr /tmp-src)
