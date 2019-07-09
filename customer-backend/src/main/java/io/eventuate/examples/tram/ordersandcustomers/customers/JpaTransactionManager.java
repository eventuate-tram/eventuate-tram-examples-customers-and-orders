package io.eventuate.examples.tram.ordersandcustomers.customers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class JpaTransactionManager<RESULT> {
  private EntityManagerFactory entityManagerFactory;
  private ConcurrentMap<Long, EntityManager> entityManagerByThreadId;

  private long currentThreadId = Thread.currentThread().getId();

  private EntityManager entityManager;
  private boolean entityManagerCreatedFlag;
  private EntityTransaction transaction;

  public JpaTransactionManager(EntityManagerFactory entityManagerFactory,
                               ConcurrentMap<Long, EntityManager> entityManagerByThreadId) {

    this.entityManagerFactory = entityManagerFactory;
    this.entityManagerByThreadId = entityManagerByThreadId;
  }

  public RESULT doWithJpaTransaction(Function<EntityManager, RESULT> function) {
    try {
      createEntityManagerIfNotExists();
      startTransactionIfEntityManagerIsJustCreated();
      RESULT result = function.apply(entityManager);
      commitTransactionIfItsCreatedByCurrentManagerInstanceAndStillActive();
      return result;
    } catch (Exception e) {
      transaction.rollback();
      throw new RuntimeException(e);
    } finally {
      forgetEntityManagerIfCreatedByCurrentManagerInstance();
    }
  }

  private void createEntityManagerIfNotExists() {
    if (!entityManagerByThreadId.containsKey(currentThreadId)) {
      entityManager = entityManagerFactory.createEntityManager();
      entityManagerByThreadId.put(currentThreadId, entityManager);
      entityManagerCreatedFlag = true;
    } else {
      entityManager = entityManagerByThreadId.get(currentThreadId);
    }
  }

  private void startTransactionIfEntityManagerIsJustCreated() {
    transaction = entityManager.getTransaction();
    if (entityManagerCreatedFlag) {
      transaction.begin();
    }
  }

  private void commitTransactionIfItsCreatedByCurrentManagerInstanceAndStillActive() {
    if (transaction.isActive() && entityManagerCreatedFlag) {
      transaction.commit();
    }
  }

  private void forgetEntityManagerIfCreatedByCurrentManagerInstance() {
    if (entityManagerCreatedFlag) {
      entityManagerByThreadId.remove(currentThreadId);
    }
  }
}
