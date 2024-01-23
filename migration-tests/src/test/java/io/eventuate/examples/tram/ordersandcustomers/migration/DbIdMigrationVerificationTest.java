package io.eventuate.examples.tram.ordersandcustomers.migration;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DbIdMigrationVerificationTest.Config.class)
public class DbIdMigrationVerificationTest {

  @Configuration
  @EnableAutoConfiguration
  public static class Config {}

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  //after first call of e2e tests (before migration), messages should have ids, after second call (after migration) don't
  public void testThatMessagesAreMigrated() {
    List<Map<String, Object>> messagesWithEmptyId =
            jdbcTemplate.queryForList("select * from eventuate.message where destination <> 'CDC-IGNORED' and id = ''");

    List<Map<String, Object>> messagesWithNotEmptyId =
            jdbcTemplate.queryForList("select * from eventuate.message where destination <> 'CDC-IGNORED' and id <> ''");

    Assert.assertTrue(messagesWithEmptyId.size() > 0);
    Assert.assertEquals(messagesWithEmptyId.size(), messagesWithNotEmptyId.size());
  }
}
