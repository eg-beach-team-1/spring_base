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
}
