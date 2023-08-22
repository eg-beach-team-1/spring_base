package com.example.domain.entity

import com.example.common.exception.BusinessException
import spock.lang.Specification

import java.time.LocalDateTime

import static com.example.domain.entity.OrderStatus.CANCELED
import static com.example.domain.entity.OrderStatus.CREATED

class OrderTest extends Specification {
    def "should calculate total price"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8)),
                                                 new ProductDetail(id: 2, name: "soda", unitPrice: BigDecimal.valueOf(10L), quantity: 1, discount: BigDecimal.valueOf(0.8))]
        def order = new Order("1", UUID.fromString("464547B0-C850-4238-BD23-1A30383CBE84"), CREATED, LocalDateTime.now(), LocalDateTime.now(), productDetailList)

        when:
        def result = order.calculateTotalPrice()

        then:
        result == BigDecimal.valueOf(30L)
    }

    def "should cancel order successfully"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8)),
                                                 new ProductDetail(id: 2, name: "soda", unitPrice: BigDecimal.valueOf(10L), quantity: 1, discount: BigDecimal.valueOf(0.8))]
        def order = new Order("1", UUID.fromString("464547B0-C850-4238-BD23-1A30383CBE84"), CREATED, LocalDateTime.now(), LocalDateTime.now(), productDetailList)

        when:
        order.cancel()

        then:
        order.status == CANCELED
    }

    def "should throw exception when order has been canceled already"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8)),
                                                 new ProductDetail(id: 2, name: "soda", unitPrice: BigDecimal.valueOf(10L), quantity: 1, discount: BigDecimal.valueOf(0.8))]
        def order = new Order("1", UUID.fromString("464547B0-C850-4238-BD23-1A30383CBE84"), CANCELED, LocalDateTime.now(), LocalDateTime.now(), productDetailList)

        when:
        order.cancel()

        then:
        thrown(BusinessException)
    }
}
