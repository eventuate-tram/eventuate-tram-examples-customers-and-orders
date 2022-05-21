FROM amazoncorretto:8u312-al2
RUN yum install -y wget tar gzip && \
  yum clean all && \
  rm -rf /var/cache/yum
RUN wget -q -O - https://dlcdn.apache.org//jmeter/binaries/apache-jmeter-5.4.3.tgz |  tar -xzf - -C /usr/local
RUN mkdir /usr/local/plans
RUN mkdir /usr/local/results
ARG serviceImageVersion
CMD java ${JAVA_OPTS} -jar jmeter-service-$serviceImageVersion.jar
ARG serviceImageVersion
COPY build/libs/jmeter-service-$serviceImageVersion.jar .
COPY TestPlan.jmx /usr/local/plans
