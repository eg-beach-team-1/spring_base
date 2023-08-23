package com.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;

public class CustomerControllerIntegrationTest extends BaseIntegrationTest {

  @Test
  @DataSet("retrieve_customer_by_id_successfully.yml")
  public void retrieve_customer_by_id_successfully() {
    given()
        .when()
        .get("/customers/{id}", "1")
        .then()
        .statusCode(OK.value())
        .body("id", equalTo("1"))
        .body("name", equalTo("client"));
  }

  @Test
  @DataSet("retrieve_customer_by_id_successfully.yml")
  public void retrieve_throw_404_is_user_not_found() {
    given()
        .when()
        .get("/customers/{id}", "2")
        .then()
        .statusCode(NOT_FOUND.value())
        .body("code", equalTo("NOT_FOUND_CUSTOMER"))
        .body("message", equalTo("Not found customer."));
  }
}
