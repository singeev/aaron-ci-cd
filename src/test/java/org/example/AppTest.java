package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class AppTest {

  @Test
  void dummyOne() {
    log.info("This is dummy test #1");
    assertTrue(true);
  }

  @Test
  void dummyTwo() {
    log.info("This is dummy test #2");
    assertTrue(true);
  }
}
