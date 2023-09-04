package com.example.domain.entity

import spock.lang.Specification

import static com.example.domain.entity.ProductStatus.VALID
import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.valueOf

class QuantityConditionTest extends Specification {
    QuantityCondition quantityCondition = Mock()

    def "should return false when total quantity is not satisfied "() {
        given:
        quantityCondition = new QuantityCondition(3, valueOf(0.7))
        def product1 = new Product(1, "name", ONE, VALID, "clothes", ONE, 10)
        def product2 = new Product(2, "name", ONE, VALID, "clothes", ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 1, (product2): 1]

        when:
        def actual = quantityCondition.isSatisfied(productIdToQuantity)

        then:
        !actual
    }

    def "should return true when total quantity is satisfied"() {
        given:
        quantityCondition = new QuantityCondition(3, valueOf(0.7))
        def product1 = new Product(1, "name", ONE, VALID, "clothes", ONE, 10)
        def product2 = new Product(2, "name", ONE, VALID, "clothes", ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 3, (product2): 1]

        when:
        def actual = quantityCondition.isSatisfied(productIdToQuantity)

        then:
        actual
    }

}
