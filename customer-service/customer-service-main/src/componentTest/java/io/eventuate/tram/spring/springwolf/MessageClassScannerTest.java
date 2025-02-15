package io.eventuate.tram.spring.springwolf;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MessageClassScannerTest {

  @Test
  public void shouldFindConcreteEventImplementations() {
    Set<Class<?>> implementations = MessageClassScanner.findConcreteImplementorsOf(TestBaseEvent.class);
    assertThat(implementations).isEqualTo(Set.of(TestConcreteEvent.class));
  }

}
