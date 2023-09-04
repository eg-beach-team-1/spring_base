package com.example.domain.entity

import spock.lang.Specification

import static com.example.domain.entity.ProductStatus.VALID
import static java.math.BigDecimal.ONE

class CategoryRangeTest extends Specification {
     CategoryRange categoryRange

    def "should return true when target category in range"() {
        given:
        categoryRange = new CategoryRange("clothes")
        def product = new Product(1, "name", ONE, VALID, "clothes", ONE, 10)

        when:
        def actual = categoryRange.belongsTo(product)

        then:
        actual
    }

    def "should return false when target category not in range"() {
        categoryRange = new CategoryRange("clothes")
        def product = new Product(1, "name", ONE, VALID, "drink", ONE, 10)

        when:
        def actual = categoryRange.belongsTo(product)

        then:
        !actual
    }
}
