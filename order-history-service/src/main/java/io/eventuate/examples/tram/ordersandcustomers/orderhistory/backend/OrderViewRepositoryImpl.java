package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.common.domain.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.events.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class OrderViewRepositoryImpl implements OrderViewRepositoryCustom {

  private MongoTemplate mongoTemplate;

  @Autowired
  public OrderViewRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void addOrder(Long orderId, Money orderTotal) {
    mongoTemplate.upsert(new Query(where("id").is(orderId)),
            new Update().set("orderTotal", orderTotal), OrderView.class);
  }

  @Override
  public void updateOrderState(Long orderId, OrderState state) {
    mongoTemplate.updateFirst(new Query(where("id").is(orderId)),
            new Update().set("state", state), OrderView.class);
  }
}
