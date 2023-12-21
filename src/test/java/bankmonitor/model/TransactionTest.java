package bankmonitor.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class TransactionTest {

  @Test
  void test_getData() {
    Transaction tr = new Transaction("{ \"reference\": \"foo\", \"amount\": 100}");

    assertEquals(tr.getReference(), "foo");
    assertEquals(tr.getAmount(), 100);
  }

  @Test
  void test_invalidData() {
    Transaction tr = new Transaction("{ \"referencee\": \"foo\", \"amount\": 100}");
    assertNotEquals(tr.getReference(), "foo");
    assertEquals(tr.getAmount(), 100);
  }

  @Test
  void test_invalidData2() {
    Transaction tr = new Transaction("{ \"kiskutya\": \"foo\", \"macska\": 100}");
    assertNotEquals(tr.getReference(), "foo");
    assertNotEquals(tr.getAmount(), 100);
  }
}