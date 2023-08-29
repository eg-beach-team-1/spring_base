package com.example.domain.entity

import spock.lang.Specification

class DiscountRuleTest extends Specification {

    Range mockRange = Mock()
    Condition mockCondition = Mock()

    def "should calculate discount correctly when product ids valid"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 5, (product2): 3]

        when:
        2 * mockRange.belongsTo(_) >> true
        1 * mockCondition.isSatisfied(_) >> true
        1 * mockCondition.getDiscount() >> 0.1 // Adjust this according to your needs

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 2
        result.every { key, value -> value == 0.1 }
    }

    def "should calculate discount correctly when one of the product ids valid"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 5, (product2): 3]

        when:
        1 * mockRange.belongsTo(product1) >> true
        1 * mockRange.belongsTo(product2) >> false
        1 * mockCondition.isSatisfied(_) >> true
        1 * mockCondition.getDiscount() >> 0.1 // Adjust this according to your needs

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 1
        result.every { key, value -> value == 0.1 }
    }

    def "should calculate discount correctly when condition is not satisfied"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 5, (product2): 3]

        when:
        2 * mockRange.belongsTo(_) >> true
        1 * mockCondition.isSatisfied(_) >> false

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 0
    }
}
