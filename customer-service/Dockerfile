ARG baseImageVersion
FROM eventuateio/eventuate-examples-docker-images-spring-example-base-image:$baseImageVersion
ARG jarDir=build/libs
COPY $jarDir/customer-service-*.jar service.jar
