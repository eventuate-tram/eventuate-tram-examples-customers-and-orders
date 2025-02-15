package io.eventuate.tram.spring.springwolf;

public class TestConcreteEvent extends AbstractTestBaseEvent {

  private final String id;

  public TestConcreteEvent(String id) {
    this.id = id;
  }

    @Override
    public String getId() {
        return "";
    }
}