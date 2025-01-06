package io.eventuate.examples.tram.sagas.ordersandcustomers.endtoendtests;

import org.jetbrains.annotations.NotNull;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseUrlUtils {
  @NotNull
  public static String baseUrl(String hostName, String path, int port, String[] pathElements) {
    assertNotNull(hostName, "host");

    StringBuilder sb = new StringBuilder("http://");
    sb.append(hostName);
    sb.append(":");
    sb.append(port);
    sb.append("/");
    sb.append(path);

    for (String pe : pathElements) {
      sb.append("/");
      sb.append(pe);
    }
    return sb.toString();
  }
}
