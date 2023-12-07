package io.eventuate.examples.tram.ordersandcustomers.migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DbIdMigrationVerificationTest.Config.class)
class DbIdMigrationVerificationTest {

  @Configuration
  @EnableAutoConfiguration
  public static class Config {}

  @Autowired
  private JdbcTemplate jdbcTemplate;

  //after first call of e2e tests (before migration), messages should have ids, after second call (after migration) don't
  @Test
  void thatMessagesAreMigrated() {
    List<Map<String, Object>> messagesWithEmptyId =
            jdbcTemplate.queryForList("select * from eventuate.message where destination <> 'CDC-IGNORED' and id = ''");

    List<Map<String, Object>> messagesWithNotEmptyId =
            jdbcTemplate.queryForList("select * from eventuate.message where destination <> 'CDC-IGNORED' and id <> ''");

    assertTrue(messagesWithEmptyId.size() > 0);
    assertEquals(messagesWithEmptyId.size(), messagesWithNotEmptyId.size());
  }
}
