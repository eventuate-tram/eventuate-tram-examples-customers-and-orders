ARG baseImageVersion
FROM eventuateio/eventuate-examples-docker-images-spring-example-base-image:$baseImageVersion
ARG jarDir=build/libs
ARG serviceImageVersion
COPY $jarDir/order-history-service-main-$serviceImageVersion.jar service.jar
