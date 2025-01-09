package io.eventuate.examples.tram.sagas.ordersandcustomers;

public class ContainerReuseUtil {

  public static boolean shouldReuse() {
    return !isCI();
  }

  private static boolean isCI() {
    return System.getenv("CI") != null;
  }
}
