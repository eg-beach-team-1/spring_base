package com.example.domain.entity

import spock.lang.Specification

class DiscountRuleTest extends Specification {

    Range mockRange = Mock()
    Condition mockCondition = Mock()

    def "should calculate discount correctly when product ids valid"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        Map<Integer, Integer> productIdToQuantity = [1: 5, 2: 3]

        when:
        2 * mockRange.belongsTo(_) >> true
        1 * mockCondition.isSatisfied(_) >> true
        1 * mockCondition.getDiscount() >> 0.1 // Adjust this according to your needs

        Map<Integer, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 2
        result.every { key, value -> value == 0.1 }
    }

    def "should calculate discount correctly when one of the product ids valid"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        Map<Integer, Integer> productIdToQuantity = [1: 5, 2: 3]

        when:
        1 * mockRange.belongsTo(1) >> true
        1 * mockRange.belongsTo(2) >> false
        1 * mockCondition.isSatisfied(_) >> true
        1 * mockCondition.getDiscount() >> 0.1 // Adjust this according to your needs

        Map<Integer, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 1
        result.every { key, value -> value == 0.1 }
    }

    def "should calculate discount correctly when condition is not satisfied"() {
        given:
        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])
        Map<Integer, Integer> productIdToQuantity = [1: 5, 2: 3]

        when:
        2 * mockRange.belongsTo(_) >> true
        1 * mockCondition.isSatisfied(_) >> false

        Map<Integer, BigDecimal> result = discountRule.calculateDiscount(productIdToQuantity)

        then:
        result.size() == 0
    }
}
