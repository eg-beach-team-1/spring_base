package com.example.domain.entity

import com.example.common.exception.BusinessException
import spock.lang.Specification

class ProductTest extends Specification {
    def "should not thrown exception when product is valid"() {
        given:
        def product = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, BigDecimal.valueOf(0.8))

        when:
        product.validateProduct()

        then:
        noExceptionThrown()
    }

    def "should thrown exception when product is invalid"() {
        given:
        def product = new Product(1, "name", BigDecimal.ONE, ProductStatus.INVALID, BigDecimal.valueOf(0.8))

        when:
        product.validateProduct()

        then:
        thrown(BusinessException)
    }

    def "should calculate discounted price successfully"() {
        given:
        def product = new Product(1, "name", BigDecimal.TEN, ProductStatus.INVALID, BigDecimal.valueOf(0.8))

        when:
        def discountedPrice = product.calculateDiscountedPrice()

        then:
        discountedPrice == BigDecimal.valueOf(8)
    }
}
