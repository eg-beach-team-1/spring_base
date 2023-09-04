package com.example;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.api.DBRider;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DBRider
@ActiveProfiles("integration-test")
@ExtendWith(DBUnitExtension.class)
@DBUnit(
    cacheConnection = false,
    schema = "spring_base",
    caseInsensitiveStrategy = LOWERCASE,
    alwaysCleanAfter = true)
@AutoConfigureWireMock(port = 0)
public abstract class BaseIntegrationTest {
  @LocalServerPort int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }

  @ServiceConnection
  private static final MySQLContainer mysql =
      new MySQLContainer(DockerImageName.parse("mysql:8.0.22"))
          .withDatabaseName("spring_base")
          .withUsername("root")
          .withPassword("password");

  static {
    mysql.start();
  }
}
