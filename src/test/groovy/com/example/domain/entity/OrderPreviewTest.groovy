package com.example.domain.entity

import spock.lang.Specification

class OrderPreviewTest extends Specification {
    def "should calculate total price"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8)),
                                                 new ProductDetail(id: 2, name: "soda", unitPrice: BigDecimal.valueOf(10L), quantity: 1, discount: BigDecimal.valueOf(0.8))]
        def orderPreview = new OrderPreview(productDetailList)

        when:
        def result = orderPreview.calculateTotalPrice()

        then:
        result == BigDecimal.valueOf(30L)
    }

    def "should calculate paid price"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8)),
                                                 new ProductDetail(id: 2, name: "soda", unitPrice: BigDecimal.valueOf(10L), quantity: 1, discount: BigDecimal.valueOf(0.8))]
        def orderPreview = new OrderPreview(productDetailList)

        when:
        def result = orderPreview.calculatePaidPrice()

        then:
        result == BigDecimal.valueOf(24L)
    }
}
