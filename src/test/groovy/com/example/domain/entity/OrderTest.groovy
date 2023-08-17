package com.example.domain.entity

import spock.lang.Specification

import java.time.LocalDateTime

class OrderTest extends Specification {
    def "should calculate total price"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8)),
                                                 new ProductDetail(id: 2, name: "soda", unitPrice: BigDecimal.valueOf(10L), quantity: 1, discount: BigDecimal.valueOf(0.8))]
        def order = new Order("1", "1", OrderStatus.CREATED, LocalDateTime.now(), LocalDateTime.now(), productDetailList, BigDecimal.valueOf(24L))

        when:
        def result = order.calculateTotalPrice()

        then:
        result == BigDecimal.valueOf(30L)

    }
}
