FROM openjdk:8u252-slim-buster
RUN apt-get update && apt-get install -y curl
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/actuator/health || exit 1
CMD java ${JAVA_OPTS} -jar order-history-text-search-service-*.jar
COPY build/libs/order-history-text-search-service-*.jar .