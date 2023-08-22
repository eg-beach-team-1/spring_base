package com.example.domain.entity

import spock.lang.Specification

class ProductDetailTest extends Specification {
    def "should calculate paid unit price correctly"() {
        given:
        def productDetail = new ProductDetail(1, "test", BigDecimal.TEN, 10, BigDecimal.valueOf(0.8))

        when:
        def result = productDetail.calculatePaidPrice()

        then:
        result == BigDecimal.valueOf(8)
    }

    def "should calculate paid paid price with 2 place decimal correctly"() {
        given:
        def productDetail = new ProductDetail(1, "test", BigDecimal.valueOf(12.42), 10, BigDecimal.valueOf(0.6))

        when:
        def result = productDetail.calculatePaidPrice()

        then:
        result == BigDecimal.valueOf(7.45)
    }

    def "should calculate paid paid price with minimum price as 0.01 correctly"() {
        given:
        def productDetail = new ProductDetail(1, "test", BigDecimal.valueOf(0.3), 10, BigDecimal.valueOf(0.01))

        when:
        def result = productDetail.calculatePaidPrice()

        then:
        result == BigDecimal.valueOf(0.01)
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
