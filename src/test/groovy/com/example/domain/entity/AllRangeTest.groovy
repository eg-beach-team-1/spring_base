package com.example.domain.entity

import spock.lang.Specification

import static com.example.domain.entity.ProductStatus.VALID
import static java.math.BigDecimal.ONE

class AllRangeTest extends Specification {
    AllRange allRange

    def "should return true whatever range product is"() {
        given:
        allRange = new AllRange()
        def product = new Product(1, "name", ONE, VALID, "clothes", ONE, 10)

        when:
        def actual = allRange.belongsTo(product)

        then:
        actual
    }
}
