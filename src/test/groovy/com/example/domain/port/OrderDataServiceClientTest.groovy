package com.example.domain.port

import com.example.domain.entity.Order
import com.example.domain.entity.OrderStatus
import com.example.domain.entity.ProductDetail
import com.example.infrastructure.client.DataServiceFeign
import com.example.infrastructure.client.MessageData
import com.example.infrastructure.client.MessageType
import com.example.infrastructure.client.Message
import com.example.infrastructure.client.OrderDataServiceClientImpl
import com.example.infrastructure.client.OrderItem
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import spock.lang.Specification

import java.time.LocalDateTime

import static java.math.BigDecimal.valueOf
import static org.junit.jupiter.api.Assertions.*

class OrderDataServiceClientTest extends Specification {

    DataServiceFeign dataServiceFeign = Mock()
    OrderDataServiceClientImpl client = new OrderDataServiceClientImpl(dataServiceFeign)

    def "should_ceate_message_from_order_and_send_message_successfully"(){
        given:
        List<ProductDetail> productDetailList = List.of(
                new ProductDetail(1, "water", valueOf(10L), null, 1, valueOf(0.8)),
                new ProductDetail(2, "water", valueOf(10L), null, 2, valueOf(0.7))
        )
        Order order = new Order(
                "order-id-1",
                UUID.fromString("464547b0-c850-4238-bd23-1a30383cbe84"),
                OrderStatus.CREATED,
                LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                productDetailList
        )

        MessageData messageData = new MessageData(
                "464547b0-c850-4238-bd23-1a30383cbe84",
                "order-id-1",
                List.of(
                        new OrderItem(1, 1, valueOf(8).setScale(2)),
                        new OrderItem(2, 2, valueOf(14).setScale(2))
                )
        )
        Message message = new Message(MessageType.ORDER_CREATION, messageData)

        dataServiceFeign.sendOrderCreationMessage(message) >> new ResponseEntity<Void>(HttpStatusCode.valueOf(200))

        when:
        client.sendOrderCreationData(order)

        then:
        1 * dataServiceFeign.sendOrderCreationMessage(message)
    }
}