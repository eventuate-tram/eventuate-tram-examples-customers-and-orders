ARG baseImageVersion
FROM eventuateio/eventuate-examples-docker-images-spring-example-base-image:$baseImageVersion
ARG serviceImageVersion
COPY build/libs/order-history-text-search-service-$serviceImageVersion.jar service.jar
