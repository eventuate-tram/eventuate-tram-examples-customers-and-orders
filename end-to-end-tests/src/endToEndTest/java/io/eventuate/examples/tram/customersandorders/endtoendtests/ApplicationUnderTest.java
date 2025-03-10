package io.eventuate.examples.tram.customersandorders.endtoendtests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;

public abstract class ApplicationUnderTest {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  public static ApplicationUnderTest make() {
    try {
      String className = ApplicationUnderTest.class.getName() + "Using" + System.getProperty("endToEndTestMode", "TestContainers");
      Class<?> clazz = ClassUtils.forName(className, ApplicationUnderTest.class.getClassLoader());
      return (ApplicationUnderTest) clazz. getDeclaredConstructor().newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
             InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public abstract void start();


  public String apiGatewayBaseUrl(String hostName, String path, String... pathElements) {
    return BaseUrlUtils.baseUrl(hostName, path, getApigatewayPort(), pathElements);
  }
  public abstract int getApigatewayPort();
  public abstract int getCustomerServicePort();
  public abstract int getOrderServicePort();
  abstract boolean exposesSwaggerUiForBackendServices();

  public abstract int getOrderHistoryServicePort();
}
