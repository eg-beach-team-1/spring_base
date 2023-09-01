package com.example.domain.entity

import spock.lang.Specification

import static com.example.domain.entity.ProductStatus.VALID
import static java.math.BigDecimal.ONE

class ProductRangeTest extends Specification {
    ProductRange productRange

    def "should return true when product id in range"() {
        given:
        productRange = new ProductRange(List.of("1", "2", "3"))
        def product = new Product(1, "name", ONE, VALID, ONE, 10)

        when:
        def actual = productRange.belongsTo(product)

        then:
        actual
    }

    def "should return false when product id not in range"() {
        given:
        productRange = new ProductRange(List.of("1", "2", "3"))
        def product = new Product(4, "name", ONE, VALID, ONE, 10)

        when:
        def actual = productRange.belongsTo(product)

        then:
        !actual
    }
}
