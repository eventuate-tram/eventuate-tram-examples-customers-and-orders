package io.eventuate.examples.tram.sagas.ordersandcustomers.apigateway.swagger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerUiTest {

  @LocalServerPort
  int port;

  private final String hostName = "localhost";

  @Configuration
  @EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
  @Import({SwaggerUiConfiguration.class})
  static public class Config {
  }

  @Test
  public void testSwaggerUiUrls() throws IOException {
    testSwaggerUiUrl(port);
  }

  @Test
  public void testSwaggerYml() throws IOException {
    assertUrlStatusIsOk("http://%s:%s/swagger/swagger.yml".formatted(hostName, port));
  }

  private void testSwaggerUiUrl(int port) throws IOException {
    assertUrlStatusIsOk("http://%s:%s/swagger-ui/index.html".formatted(hostName, port));
  }

  private void assertUrlStatusIsOk(String url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    if (connection.getResponseCode() != 200)
      Assertions.fail("Expected 200 for %s, got %s".formatted(url, connection.getResponseCode()));
  }


}