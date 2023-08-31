package com.example;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;

import com.example.domain.feign.Message;
import com.example.infrastructure.persistence.port.OrderDataServiceFeignImpl;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class OrderDataServiceFeignIntegrationTest extends BaseIntegrationTest {

  @Autowired private OrderDataServiceFeignImpl orderDataServiceFeign;

  @Test
  public void should_send_request_successfully() {
    JSONObject messageJson = new JSONObject();
    messageJson.put("message", "message content");
    String messageBody = messageJson.toJSONString();
    stubFor(
        post(urlPathEqualTo("/data-service/messages"))
            .withRequestBody(equalToJson(messageBody))
            .willReturn(aResponse().withStatus(200)));

    Message message = new Message("message content");

    ResponseEntity<Void> response = orderDataServiceFeign.sendOrderCreationData(message);

    assertEquals(OK, response.getStatusCode());
  }
}
