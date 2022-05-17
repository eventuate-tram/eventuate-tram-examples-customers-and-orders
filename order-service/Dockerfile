ARG baseImageVersion
FROM eventuateio/eventuate-examples-docker-images-spring-example-base-image:$baseImageVersion
ARG jarDir=build/libs
ARG serviceImageVersion
COPY $jarDir/order-service-$serviceImageVersion.jar service.jar
