package com.example.domain.entity

import spock.lang.Specification

import java.time.LocalDateTime

class OrderTest extends Specification {
    def "should calculate total price"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", price: BigDecimal.valueOf(10L), amount: 2),
                                                 new ProductDetail(id: 2, name: "soda", price: BigDecimal.valueOf(10L), amount: 1)]
        def order = new Order("1", "1", OrderStatus.CREATED, LocalDateTime.now(), LocalDateTime.now(), productDetailList)

        when:
        def result = order.calculateTotalPrice()

        then:
        result == BigDecimal.valueOf(30L)

    }
}
