package com.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;

public class ProductControllerIntegrationTest extends BaseIntegrationTest {

  @Test
  @DataSet("retrieve_products_on_product_table.yml")
  public void should_retrieve_all_products_successfully() {
    given()
        .when()
        .get("/products")
        .then()
        .statusCode(OK.value())
        .body("[0].id", equalTo(10))
        .body("[0].name", equalTo("book"))
        .body("[0].price", equalTo(10.00))
        .body("[0].status", equalTo("VALID"))
        .body("[0].discount",equalTo(0.8))
        .body("[0].stock",equalTo(1));
  }
}
