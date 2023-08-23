package com.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.github.database.rider.core.api.dataset.DataSet;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderControllerIntegrationTest extends BaseIntegrationTest {

  @Test
  @DataSet("retrieve_orders_on_order_table.yml")
  public void should_retrieve_order_list_by_customer_id_successfully() {
    given()
        .when()
        .get("/orders?customerId=dcabcfac-6b08-47cd-883a-76c5dc366d88")
        .then()
        .statusCode(OK.value())
        .body("[0].orderId", equalTo("546f4304-3be2-11ee-be56-0242ac120001"))
        .body("[0].customerId", equalTo("dcabcfac-6b08-47cd-883a-76c5dc366d88"))
        .body("[0].totalPrice", equalTo(20))
        .body("[0].paidPrice", equalTo(16.0F))
        .body("[0].status", equalTo("CREATED"))
        .body("[0].createTime", equalTo("2023-08-10T12:35:13"))
        .body("[0].productDetails[0].name", equalTo("water"))
        .body("[0].productDetails[0].discountedPrice", equalTo(8.0F))
        .body("[0].productDetails[0].priceDifference", equalTo(4.0F))
        .body("[1].orderId", equalTo("546f4304-3be2-11ee-be56-0242ac120002"))
        .body("[1].customerId", equalTo("dcabcfac-6b08-47cd-883a-76c5dc366d88"))
        .body("[1].totalPrice", equalTo(40))
        .body("[1].paidPrice", equalTo(32.0F))
        .body("[1].status", equalTo("CREATED"))
        .body("[1].createTime", equalTo("2023-08-10T12:35:13"))
        .body("[1].productDetails[0].name", equalTo("cola"))
        .body("[1].productDetails[0].discountedPrice", equalTo(16.0F))
        .body("[1].productDetails[0].priceDifference", equalTo(8.0F))
        .body("size()", equalTo(2));
  }

  @Test
  @DataSet("retrieve_orders_on_order_table.yml")
  public void should_retrieve_order_list_by_customer_id_and_order_id_successfully() {
    given()
        .when()
        .get("/orders/{orderId}", "546f4304-3be2-11ee-be56-0242ac120001")
        .then()
        .statusCode(OK.value())
        .body("orderId", equalTo("546f4304-3be2-11ee-be56-0242ac120001"))
        .body("customerId", equalTo("dcabcfac-6b08-47cd-883a-76c5dc366d88"))
        .body("totalPrice", equalTo(20))
        .body("paidPrice", equalTo(16.0F))
        .body("status", equalTo("CREATED"))
        .body("createTime", equalTo("2023-08-10T12:35:13"))
        .body("productDetails[0].name", equalTo("water"))
        .body("productDetails[0].discountedPrice", equalTo(8.0F))
        .body("productDetails[0].priceDifference", equalTo(4.0F));
  }

  @Test
  @DataSet("save_order_successfully.yml")
  public void should_save_order_and_return_order_id_successfully() {

    JSONObject orderProductOne = new JSONObject();
    orderProductOne.putAll(Map.of("productId", 1001, "quantity", 1L));
    JSONObject orderProductTwo = new JSONObject();
    orderProductTwo.putAll(Map.of("productId", 1002, "quantity", 2L));
    JSONObject orderProductThree = new JSONObject();
    orderProductThree.putAll(Map.of("productId", 1003, "quantity", 3L));

    JSONArray orderProducts = new JSONArray();
    orderProducts.addAll(List.of(orderProductOne, orderProductTwo, orderProductThree));

    JSONObject orderRequest = new JSONObject();
    orderRequest.putAll(
        Map.of(
            "customerId", "dcabcfac-6b08-47cd-883a-76c5dc366d88", "orderProducts", orderProducts));
    String orderReqBody = orderRequest.toJSONString();

    Response response =
        given().contentType(ContentType.JSON).body(orderReqBody).when().post("/orders");
    Assertions.assertEquals(CREATED.value(), response.statusCode());
    Assertions.assertNotNull(response.body());
  }

  @Test
  @DataSet("retrieve_orders_on_order_table.yml")
  public void should_cancel_order_by_order_id_and_customer_id_successfully() {
    given()
        .when()
        .patch(
            "/orders/{orderId}?customerId=dcabcfac-6b08-47cd-883a-76c5dc366d88",
            "546f4304-3be2-11ee-be56-0242ac120001")
        .then()
        .statusCode(OK.value());
  }

  @Test
  @DataSet("retrieve_orders_on_order_table.yml")
  public void should_throw_404_when_order_not_in_db() {
    given()
        .when()
        .patch(
            "/orders/{orderId}?customerId=dcabcfac-6b08-47cd-883a-76c5dc366d88",
            "546f4304-3be2-11ee-be56-0242ac121111")
        .then()
        .statusCode(NOT_FOUND.value())
        .body("code", equalTo("NOT_FOUND_ORDER"))
        .body("message", equalTo("Order not found."));
  }
}
