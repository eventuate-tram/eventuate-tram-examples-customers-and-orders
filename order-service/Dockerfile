ARG baseImageVersion
FROM eventuateio/eventuate-examples-docker-images-spring-example-base-image:$baseImageVersion
ARG jarDir=build/libs
COPY $jarDir/order-service-*.jar service.jar
