package io.github.alvarorg14.kashy.api.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * TestContainers configuration for PostgreSQL integration tests.
 *
 * <p>Provides a PostgreSQL container that starts automatically for integration tests. The container
 * is shared across tests in the same JVM for better performance.
 */
public abstract class AbstractIT {

  static final PostgreSQLContainer postgres;

  static {
    postgres =
        new PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("kashy")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);
    postgres.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("POSTGRESQL_URL", postgres::getJdbcUrl);
    registry.add("POSTGRESQL_USERNAME", postgres::getUsername);
    registry.add("POSTGRESQL_PASSWORD", postgres::getPassword);
  }
}
