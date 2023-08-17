package com.example.domain.entity

import spock.lang.Specification

class ProductDetailTest extends Specification {
    def "should calculate paid correctly"() {
        given:
        def productDetail = new ProductDetail(1, "test", BigDecimal.TEN, 10, BigDecimal.valueOf(0.8))

        when:
        def result = productDetail.calculatePaidPrice()

        then:
        result == BigDecimal.valueOf(80)
    }

    def "should calculate price difference correctly"() {
        given:
        def productDetail = new ProductDetail(1, "test", BigDecimal.TEN, 10, BigDecimal.valueOf(0.8))

        when:
        def result = productDetail.calculatePriceDifference()

        then:
        result == BigDecimal.valueOf(20)
    }
}
