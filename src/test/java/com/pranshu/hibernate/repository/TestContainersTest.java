package com.pranshu.hibernate.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainersTest {

  // Clean database for every test run
  @Container
  PostgreSQLContainer container = new PostgreSQLContainer("postgres:16-alpine")
    .withDatabaseName("testcontainersdb")
    .withUsername("tcuser")
    .withPassword("tcsecret");

  @Test
  void testContainersIsRunningTest() {
    Assertions.assertTrue(container.isCreated());
    Assertions.assertTrue(container.isRunning());
  }
}
