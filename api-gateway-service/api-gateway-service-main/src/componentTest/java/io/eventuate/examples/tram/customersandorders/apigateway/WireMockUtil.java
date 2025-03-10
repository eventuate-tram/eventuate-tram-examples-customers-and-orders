package io.eventuate.examples.tram.customersandorders.apigateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import java.util.function.Supplier;

public class WireMockUtil {
  static Supplier<Object> makeServiceUrlSupplier(WireMockServer server) {
    return () -> "http://localhost:" + server.port();
  }

  static WireMockServer mockOnDynamicPort() {
    return new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
  }
}
