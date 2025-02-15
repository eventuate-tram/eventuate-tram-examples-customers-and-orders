package io.eventuate.tram.spring.springwolf;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessageClassScanner {
  public static Set<Class<?>> findConcreteImplementorsOf(Class<?> eventBaseClass) {
    if (eventBaseClass == null) {
      throw new IllegalArgumentException("eventBaseClass cannot be null");
    }

    if (java.lang.reflect.Modifier.isFinal(eventBaseClass.getModifiers())) {
      System.out.println("[DEBUG_LOG] Class " + eventBaseClass.getName() + " is final, cannot have implementations");
      return Set.of();
    }

    Package pkg = eventBaseClass.getPackage();
    if (pkg == null) {
      throw new IllegalArgumentException("eventBaseClass must be in a package");
    }

    try {
      ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

      String basePackage = pkg.getName();

      scanner.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(basePackage.replace(".", "\\.") + "\\.[^.]+$")));

      return scanner.findCandidateComponents(basePackage)
          .stream()
          .map(beanDefinition -> {
            try {
              return Thread.currentThread().getContextClassLoader().loadClass(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
              return null;
            }
          })
          .filter(Objects::nonNull)
          .filter(clazz -> isConcreteImplementor(eventBaseClass, clazz))
          .collect(Collectors.toSet());
    } catch (Exception e) {
      throw new RuntimeException("Failed to scan for concrete implementations", e);
    }
  }

  private static boolean isConcreteImplementor(Class<?> eventBaseClass, Class<?> clazz) {
    return !clazz.equals(eventBaseClass) && !clazz.isInterface() && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())
        && eventBaseClass.isAssignableFrom(clazz);
  }
}
