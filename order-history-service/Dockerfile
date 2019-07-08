FROM java:openjdk-8u111-alpine
RUN apk --no-cache add curl
HEALTHCHECK --start-period=30s --interval=5s CMD curl -f http://localhost:8080/actuator/health || exit 1
CMD java ${JAVA_OPTS} -jar order-history-service-*.jar
COPY build/libs/order-history-service-*.jar .
