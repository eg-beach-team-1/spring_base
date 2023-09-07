package com.example.domain.entity

import spock.lang.Specification

class DiscountRuleTest extends Specification {

    Range mockRange = Mock()
    Condition mockCondition = Mock()

    def "should calculate discount correctly when all product ids in range and satisfy condition"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 5, (product2): 3]

        when:
        2 * mockRange.belongsTo(_) >> true
        1 * mockCondition.isSatisfied(_) >> true
        1 * mockCondition.getDiscount() >> 0.1

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 2
        result.every { key, value -> value == 0.1 }
    }

    def "should calculate discount correctly when one of the product ids in range and satisfy condition"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 5, (product2): 3]

        when:
        1 * mockRange.belongsTo(product1) >> true
        1 * mockRange.belongsTo(product2) >> false
        1 * mockCondition.isSatisfied(_) >> true
        1 * mockCondition.getDiscount() >> 0.1

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 2
        result.get(product1) == 0.1
        result.get(product2) == 1.0
    }

    def "should calculate discount correctly when condition is not satisfied"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 5, (product2): 3]

        when:
        2 * mockRange.belongsTo(_) >> true
        1 * mockCondition.isSatisfied(_) >> false

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 2
        result.every { key, value -> value == 1.0 }
    }

    def "should calculate discount correctly when product is not in range"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 5, (product2): 3]

        when:
        2 * mockRange.belongsTo(_) >> false
        1 * mockCondition.isSatisfied(_) >> true
        1 * mockCondition.getDiscount() >> 0.8

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 2
        result.every { key, value -> value == 1.0 }
    }

    def "should calculate discount correctly when top priority condition satisfied"() {
        given:
        def condition1 = new QuantityCondition(5, BigDecimal.valueOf(0.7))
        def condition2 = new QuantityCondition(3, BigDecimal.valueOf(0.8))
        def quantityCondition = List.of(condition1, condition2)
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: quantityCondition)
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 3, (product2): 3]

        when:
        2 * mockRange.belongsTo(_) >> true

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 2
        result.every { key, value -> value == 0.7 }
    }

    def "should calculate discount correctly when lower priority condition satisfied"() {
        given:
        def condition1 = new QuantityCondition(5, BigDecimal.valueOf(0.7))
        def condition2 = new QuantityCondition(3, BigDecimal.valueOf(0.8))
        def quantityCondition = List.of(condition1, condition2)
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: quantityCondition)
        def product1 = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        def product2 = new Product(2, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.ONE, 10)
        Map<Product, Integer> productIdToQuantity = [(product1): 1, (product2): 3]

        when:
        2 * mockRange.belongsTo(_) >> true

        Map<Product, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 2
        result.every { key, value -> value == 0.8 }
    }
}
