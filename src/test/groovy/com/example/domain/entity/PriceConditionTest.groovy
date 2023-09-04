package com.example.domain.entity

import spock.lang.Specification

import static com.example.domain.entity.ProductStatus.VALID
import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.valueOf

class PriceConditionTest extends Specification {
    PriceCondition priceCondition = Mock()

    def "should return false when total price is not satisfied "() {
        given:
        priceCondition = new PriceCondition(valueOf(500), valueOf(0.7))
        def product1 = new Product(1, "name", valueOf(100), VALID, "clothes", ONE, 10)
        def product2 = new Product(2, "name", valueOf(200), VALID, "clothes", ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 1, (product2): 1]

        when:
        def actual = priceCondition.isSatisfied(productIdToQuantity)

        then:
        !actual
    }

    def "should return true when total price is satisfied"() {
        given:
        priceCondition = new PriceCondition(valueOf(500), valueOf(0.7))
        def product1 = new Product(1, "name", valueOf(100), VALID, "clothes", ONE, 10)
        def product2 = new Product(2, "name", valueOf(200), VALID, "clothes", ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 1, (product2): 3]

        when:
        def actual = priceCondition.isSatisfied(productIdToQuantity)

        then:
        actual
    }
}
