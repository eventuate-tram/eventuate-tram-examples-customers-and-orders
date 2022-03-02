ARG baseImageVersion
FROM amazoncorretto:8u312-al2

RUN yum install -y jq && \
  amazon-linux-extras install docker && \
  curl -L https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose && \
  chmod +x /usr/local/bin/docker-compose && \
  rm -rf /var/lib/apt/lists/* && \
  yum clean all && \
  rm -rf /var/cache/yum


WORKDIR /eventuate-tram-examples-customers-and-orders
CMD echo Ready ; sleep 260000

COPY build /tmp-src

RUN echo "Building in image..." && (cd /tmp-src ; ./gradlew testClasses assemble ; cd ; rm -fr /tmp-src)
