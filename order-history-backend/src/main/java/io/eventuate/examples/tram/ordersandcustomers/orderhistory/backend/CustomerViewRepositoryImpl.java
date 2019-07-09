package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.MoneyDTO;
import io.eventuate.examples.tram.ordersandcustomers.commondomain.OrderState;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class CustomerViewRepositoryImpl implements CustomerViewRepositoryCustom {

  private MongoTemplate mongoTemplate;

  @Autowired
  public CustomerViewRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void addCustomer(Long customerId, String customerName, MoneyDTO creditLimit) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)),
            new Update().set("name", customerName).set("creditLimit", creditLimit), CustomerView.class);
  }

  @Override
  public void addOrder(Long customerId, Long orderId, MoneyDTO orderTotal) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)),
            new Update().set("orders." + orderId, new OrderInfo(orderId, orderTotal)), CustomerView.class);
  }

  @Override
  public void updateOrderState(Long customerId, Long orderId, OrderState state) {
    mongoTemplate.upsert(new Query(where("id").is(customerId)),
            new Update().set("orders." + orderId + ".state", state), CustomerView.class);
  }
}
