package com.example.infrastructure.client

import com.example.domain.entity.Order
import com.example.domain.entity.OrderStatus
import com.example.domain.entity.ProductDetail
import org.junit.jupiter.api.Assertions
import spock.lang.Specification

import java.time.LocalDateTime

import static java.math.BigDecimal.valueOf

class MessageDataMapperTest extends Specification {
    MessageDataMapper mapper = MessageDataMapper.MAPPER

    def "should_map_message_data_successfully"() {
        given:
        MessageData messageDataExpected = new MessageData(
                "464547b0-c850-4238-bd23-1a30383cbe84",
                "order-id-1",
                List.of(
                        new OrderItem(1, 1, valueOf(8).setScale(2)),
                        new OrderItem(2, 2, valueOf(14).setScale(2))
                )
        )

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

        when:
        MessageData messageData = mapper.toMessageDataFromOrder(order)

        then:
        Assertions.assertEquals(messageDataExpected, messageData)

    }
}