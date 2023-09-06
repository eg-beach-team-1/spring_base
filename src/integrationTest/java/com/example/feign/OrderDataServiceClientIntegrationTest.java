package com.example.feign;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;

import com.example.BaseIntegrationTest;
import com.example.domain.entity.Order;
import com.example.domain.entity.OrderStatus;
import com.example.domain.entity.ProductDetail;
import com.example.infrastructure.client.OrderDataServiceClientImpl;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class OrderDataServiceClientIntegrationTest extends BaseIntegrationTest {

  @Autowired private OrderDataServiceClientImpl orderDataServiceFeign;

  @Test
  public void should_send_request_successfully() {

    String messageBody =
        """
                {
                "messageType": "ORDER_CREATION",
                "data": {
                            "customerId": "464547b0-c850-4238-bd23-1a30383cbe84",
                            "orderId": "order-id-1",
                            "orderItems": [
                                {"productId": 1, "quantity": 1, "salePrice": 8.00},
                                {"productId": 2, "quantity": 2, "salePrice": 14.00}
                            ]
                         }
                }""";

    stubFor(post(urlPathEqualTo("/messages")).willReturn(aResponse().withStatus(200)));

    List<ProductDetail> productDetailList =
        List.of(
            new ProductDetail(1, "water", valueOf(10L), null, 1, valueOf(0.8)),
            new ProductDetail(2, "water", valueOf(10L), null, 2, valueOf(0.7)));
    Order order =
        new Order(
            "order-id-1",
            UUID.fromString("464547b0-c850-4238-bd23-1a30383cbe84"),
            OrderStatus.CREATED,
            LocalDateTime.of(2023, 8, 8, 10, 30, 0),
            LocalDateTime.of(2023, 8, 8, 10, 30, 0),
            productDetailList);

    ResponseEntity<Void> response = orderDataServiceFeign.sendOrderCreationData(order);

    assertEquals(OK, response.getStatusCode());
    WireMock.verify(
        postRequestedFor(urlPathEqualTo("/messages")).withRequestBody(equalToJson(messageBody)));
  }
}
