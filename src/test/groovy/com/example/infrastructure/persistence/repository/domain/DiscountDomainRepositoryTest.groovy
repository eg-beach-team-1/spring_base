package com.example.infrastructure.persistence.repository.domain


import com.example.infrastructure.persistence.assembler.DiscountDataMapper
import com.example.infrastructure.persistence.entity.DiscountRulePo
import com.example.infrastructure.persistence.repository.JpaDiscountRuleRepository
import spock.lang.Specification

class DiscountDomainRepositoryTest extends Specification {

    JpaDiscountRuleRepository jpaDiscountRuleRepository = Mock()
    DiscountDataMapper discountDataMapper = new DiscountDataMapper()
    DiscountDomainRepository discountDomainRepository = new DiscountDomainRepository(jpaDiscountRuleRepository, discountDataMapper)

    def "should find discount rule with quantity condition successfully"() {
        given:
        DiscountRulePo mockDiscountRulePo = new DiscountRulePo()
        mockDiscountRulePo.setRuleId("rule1")
        mockDiscountRulePo.setDiscountRange("""
            {
              "productIds": [
              "123",
              "456"
              ]
            }
        """)
        mockDiscountRulePo.setDiscountConditions(
                """
        [
            {
              "quantity": 10,
              "discount": 3.2,
              "priority": 2
            }, {
              "quantity": 20,
              "discount": 2.2,
              "priority": 1
            }
            ]
        """)
        jpaDiscountRuleRepository.findAll() >> [mockDiscountRulePo]

        when:
        def discountRule = discountDomainRepository.findDiscountRule().get()

        then:
        discountRule.range.productIds.get(0) == "123"
        discountRule.range.productIds.get(1) == "456"
        discountRule.conditions.get(0).getDiscount() == BigDecimal.valueOf(2.2)
        discountRule.conditions.get(0).getQuantity() == 20
        discountRule.conditions.get(1).getDiscount() == BigDecimal.valueOf(3.2)
        discountRule.conditions.get(1).getQuantity() == 10
    }

    def "should find discount rule with price condition successfully"() {
        given:
        DiscountRulePo mockDiscountRulePo = new DiscountRulePo()
        mockDiscountRulePo.setRuleId("rule1")
        mockDiscountRulePo.setDiscountRange(
                """
                     {
                       "productIds": [
                       "123",
                       "456"
                       ]
                     }
                """)
        mockDiscountRulePo.setDiscountConditions(
                """
                    [
                        {
                          "price": 200.0,
                          "discount": 0.8,
                          "priority": 2
                        }, {
                          "price": 500.0,
                          "discount": 0.7,
                          "priority": 1
                        }
                        ]
                """)
        jpaDiscountRuleRepository.findAll() >> [mockDiscountRulePo]

        when:
        def discountRule = discountDomainRepository.findDiscountRule().get()

        then:
        discountRule.range.productIds.get(0) == "123"
        discountRule.range.productIds.get(1) == "456"
        discountRule.conditions.get(0).getDiscount() == BigDecimal.valueOf(0.7)
        discountRule.conditions.get(0).getPrice() == BigDecimal.valueOf(500)
        discountRule.conditions.get(1).getDiscount() == BigDecimal.valueOf(0.8)
        discountRule.conditions.get(1).getPrice() == BigDecimal.valueOf(200)
    }

    def "should find empty discount rule successfully when no rule is set"() {
        given:
        jpaDiscountRuleRepository.findAll() >> List.of()

        when:
        def discountRule = discountDomainRepository.findDiscountRule()

        then:
        discountRule.isEmpty()
    }
}
