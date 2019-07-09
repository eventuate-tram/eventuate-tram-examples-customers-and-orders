package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.common.jdbc.micronaut.EventuateMicronautTransactionManagement;
import org.hibernate.internal.SessionImpl;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

@Singleton
public class EventuateMicronautJpaTransactionManagement implements EventuateMicronautTransactionManagement {

  @Inject
  private EntityManagerFactory entityManagerFactory;

  private ConcurrentMap<Long, EntityManager> entityManagerByThreadId = new ConcurrentHashMap<>();

  public void doWithJpaTransaction(Consumer<EntityManager> consumer) {
    new JpaTransactionManager<Void>(entityManagerFactory, entityManagerByThreadId).doWithJpaTransaction(entityManager -> {
      consumer.accept(entityManager);
      return null;
    });
  }

  public <RESULT> RESULT doWithJpaTransaction(Function<EntityManager, RESULT> consumer) {
    return new JpaTransactionManager<RESULT>(entityManagerFactory, entityManagerByThreadId).doWithJpaTransaction(consumer);
  }

  @Override
  public void doWithTransaction(Consumer<Connection> consumer) {
    doWithJpaTransaction(entityManager -> {
      SessionImpl delegate = (SessionImpl) entityManager.getDelegate();
      consumer.accept(delegate.connection());
    });
  }
}
