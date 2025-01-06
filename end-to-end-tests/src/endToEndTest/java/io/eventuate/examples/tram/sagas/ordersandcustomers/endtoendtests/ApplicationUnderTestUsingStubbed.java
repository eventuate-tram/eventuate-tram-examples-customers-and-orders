package io.eventuate.examples.tram.sagas.ordersandcustomers.endtoendtests;

public class ApplicationUnderTestUsingStubbed extends ApplicationUnderTest {
    @Override
    public void start() {

    }

    @Override
    public int getApigatewayPort() {
        return 8081;
    }

    @Override
    public int getCustomerServicePort() {
        return 8082;
    }

    @Override
    public int getOrderServicePort() {
        return 8083;
    }

    @Override
    boolean exposesSwaggerUiForBackendServices() {
        return false;
    }
}
