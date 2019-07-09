package io.eventuate.examples.tram.ordersandcustomers.commondomain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;

public class MoneyDTO {

  public static final MoneyDTO ZERO = new MoneyDTO(0);
  private BigDecimal amount;

  public MoneyDTO() {
  }

  public MoneyDTO(int i) {
    this.amount = new BigDecimal(i);
  }
  public MoneyDTO(String s) {
    this.amount = new BigDecimal(s);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public MoneyDTO(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public boolean isGreaterThanOrEqual(MoneyDTO other) {
    return amount.compareTo(other.amount) >= 0;
  }

  public MoneyDTO add(MoneyDTO other) {
    return new MoneyDTO(amount.add(other.amount));
  }
  public MoneyDTO subtract(MoneyDTO other) {
    return new MoneyDTO(amount.subtract(other.amount));
  }
}