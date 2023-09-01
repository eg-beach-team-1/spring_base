package com.example;

import static io.restassured.RestAssured.given;
import static java.util.List.of;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import io.restassured.http.ContentType;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
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
        .body("[0].productDetails[0].discountedPrice", equalTo(16.0F))
        .body("[0].productDetails[0].priceDifference", equalTo(4.0F))
        .body("[1].orderId", equalTo("546f4304-3be2-11ee-be56-0242ac120002"))
        .body("[1].customerId", equalTo("dcabcfac-6b08-47cd-883a-76c5dc366d88"))
        .body("[1].totalPrice", equalTo(40))
        .body("[1].paidPrice", equalTo(32.0F))
        .body("[1].status", equalTo("CREATED"))
        .body("[1].createTime", equalTo("2023-08-10T12:35:13"))
        .body("[1].productDetails[0].name", equalTo("cola"))
        .body("[1].productDetails[0].discountedPrice", equalTo(32.0F))
        .body("[1].productDetails[0].priceDifference", equalTo(8.0F))
        .body("size()", equalTo(3));
  }

  @Test
  @DataSet("retrieve_orders_on_order_table.yml")
  public void should_retrieve_order_list_by_customer_id_and_order_id_successfully() {
    given()
        .when()
        .get(
            "/orders/{orderId}?customerId=dcabcfac-6b08-47cd-883a-76c5dc366d88",
            "546f4304-3be2-11ee-be56-0242ac120001")
        .then()
        .statusCode(OK.value())
        .body("orderId", equalTo("546f4304-3be2-11ee-be56-0242ac120001"))
        .body("customerId", equalTo("dcabcfac-6b08-47cd-883a-76c5dc366d88"))
        .body("totalPrice", equalTo(20))
        .body("paidPrice", equalTo(16.0F))
        .body("status", equalTo("CREATED"))
        .body("createTime", equalTo("2023-08-10T12:35:13"))
        .body("productDetails[0].name", equalTo("water"))
        .body("productDetails[0].discountedPrice", equalTo(16.0F))
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
    orderProducts.addAll(of(orderProductOne, orderProductTwo, orderProductThree));

    JSONObject orderRequest = new JSONObject();
    orderRequest.putAll(
        Map.of(
            "customerId", "dcabcfac-6b08-47cd-883a-76c5dc366d88", "orderProducts", orderProducts));
    String orderReqBody = orderRequest.toJSONString();

    given()
        .contentType(ContentType.JSON)
        .body(orderReqBody)
        .when()
        .post("/orders")
        .then()
        .statusCode(CREATED.value());
  }

  @Test
  @DataSet("save_order_successfully.yml")
  public void should_throw_404_given_product_not_found() {

    JSONObject orderProductOne = new JSONObject();
    orderProductOne.putAll(Map.of("productId", 2000, "quantity", 1L));

    JSONArray orderProducts = new JSONArray();
    orderProducts.addAll(of(orderProductOne));

    JSONObject orderRequest = new JSONObject();
    orderRequest.putAll(
        Map.of(
            "customerId", "dcabcfac-6b08-47cd-883a-76c5dc366d88", "orderProducts", orderProducts));
    String orderReqBody = orderRequest.toJSONString();

    given()
        .contentType(ContentType.JSON)
        .body(orderReqBody)
        .when()
        .post("/orders")
        .then()
        .statusCode(NOT_FOUND.value())
        .body("code", equalTo("NOT_FOUND_PRODUCT"))
        .body("message", equalTo("Not found product."));
  }

  @Test
  @DataSet("save_order_successfully.yml")
  public void should_throw_403_given_product_not_validated() {

    JSONObject orderProductOne = new JSONObject();
    orderProductOne.putAll(Map.of("productId", 1004, "quantity", 1L));

    JSONArray orderProducts = new JSONArray();
    orderProducts.addAll(of(orderProductOne));

    JSONObject orderRequest = new JSONObject();
    orderRequest.putAll(
        Map.of(
            "customerId", "dcabcfac-6b08-47cd-883a-76c5dc366d88", "orderProducts", orderProducts));
    String orderReqBody = orderRequest.toJSONString();

    given()
        .contentType(ContentType.JSON)
        .body(orderReqBody)
        .when()
        .post("/orders")
        .then()
        .statusCode(FORBIDDEN.value())
        .body("code", equalTo("INVALID_PRODUCT"))
        .body("message", equalTo("Invalid product."));
  }

  @Test
  @DataSet("save_order_successfully.yml")
  public void should_throw_422_given_product_not_enough() {

    JSONObject orderProductOne = new JSONObject();
    orderProductOne.putAll(Map.of("productId", 1003, "quantity", 100L));

    JSONArray orderProducts = new JSONArray();
    orderProducts.addAll(of(orderProductOne));

    JSONObject orderRequest = new JSONObject();
    orderRequest.putAll(
        Map.of(
            "customerId", "dcabcfac-6b08-47cd-883a-76c5dc366d88", "orderProducts", orderProducts));
    String orderReqBody = orderRequest.toJSONString();

    given()
        .contentType(ContentType.JSON)
        .body(orderReqBody)
        .when()
        .post("/orders")
        .then()
        .statusCode(UNPROCESSABLE_ENTITY.value())
        .body("code", equalTo("PRODUCT_STOCK_SHORTAGE"))
        .body("message", equalTo("the stock of this product is less than the amount."));
  }

  @Test
  @DataSet("save_order_successfully.yml")
  public void should_throw_422_given_product_has_zero_stock() {

    JSONObject orderProductOne = new JSONObject();
    orderProductOne.putAll(Map.of("productId", 1005, "quantity", 1L));

    JSONArray orderProducts = new JSONArray();
    orderProducts.addAll(of(orderProductOne));

    JSONObject orderRequest = new JSONObject();
    orderRequest.putAll(
        Map.of(
            "customerId", "dcabcfac-6b08-47cd-883a-76c5dc366d88", "orderProducts", orderProducts));
    String orderReqBody = orderRequest.toJSONString();

    given()
        .contentType(ContentType.JSON)
        .body(orderReqBody)
        .when()
        .post("/orders")
        .then()
        .statusCode(UNPROCESSABLE_ENTITY.value())
        .body("code", equalTo("OUT_OF_STOCK"))
        .body("message", equalTo("this product is out of stock."));
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

  @Test
  @DataSet("retrieve_orders_on_order_table.yml")
  @ExpectedDataSet(
      value = "after_cancel_the_order.yml",
      ignoreCols = {"update_time"})
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
  public void should_throw_409_if_order_is_already_canceled() {
    given()
        .when()
        .patch(
            "/orders/{orderId}?customerId=dcabcfac-6b08-47cd-883a-76c5dc366d88",
            "546f4304-3be2-11ee-be56-0242ac120003")
        .then()
        .statusCode(CONFLICT.value())
        .body("code", equalTo("ALREADY_CANCELED_ORDER"))
        .body("message", equalTo("This order has been canceled already."));
  }

  @Test
  @DataSet("preview_order_successfully.yml")
  public void should_preview_order_successfully() {
    JSONObject orderProductOne = new JSONObject();
    orderProductOne.putAll(Map.of("productId", 1001, "quantity", 2L));

    JSONArray orderProducts = new JSONArray();
    orderProducts.addAll(of(orderProductOne));

    JSONObject orderRequest = new JSONObject();
    orderRequest.putAll(
        Map.of(
            "customerId", "dcabcfac-6b08-47cd-883a-76c5dc366d88", "orderProducts", orderProducts));
    String orderReqBody = orderRequest.toJSONString();
    given()
        .contentType(ContentType.JSON)
        .body(orderReqBody)
        .when()
        .post("/orders/preview")
        .then()
        .statusCode(OK.value())
        .body("totalPrice", equalTo(20.0F))
        .body("paidPrice", equalTo(14.0F))
        .body("productDetails[0].id", equalTo(1001))
        .body("productDetails[0].name", equalTo("book"))
        .body("productDetails[0].unitPrice", equalTo(10.0F))
        .body("productDetails[0].quantity", equalTo(2))
        .body("productDetails[0].discount", equalTo(0.7F))
        .body("productDetails[0].category", equalTo("book"))
        .body("productDetails[0].discountedPrice", equalTo(14.0F))
        .body("productDetails[0].priceDifference", equalTo(6.0F));
  }
}
