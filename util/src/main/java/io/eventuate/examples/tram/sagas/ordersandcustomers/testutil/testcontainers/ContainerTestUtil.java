package io.eventuate.examples.tram.sagas.ordersandcustomers.testutil.testcontainers;

import java.time.Duration;

public class ContainerTestUtil {

  public static boolean shouldReuse() {
    return !isCI();
  }

  private static boolean isCI() {
    return System.getenv("CI") != null;
  }

  public static Duration startupTimeout() {
    return Duration.ofSeconds(isCI() ? 120 : 60);
  }
}
