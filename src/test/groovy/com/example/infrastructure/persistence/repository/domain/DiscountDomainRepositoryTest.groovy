package com.example.infrastructure.persistence.repository.domain


import com.example.infrastructure.persistence.assembler.DiscountDataMapper
import com.example.infrastructure.persistence.entity.DiscountRulePo
import com.example.infrastructure.persistence.repository.JpaDiscountRuleRepository
import spock.lang.Specification

class DiscountDomainRepositoryTest extends Specification {

    JpaDiscountRuleRepository jpaDiscountRuleRepository = Mock()

    DiscountDataMapper discountDataMapper = new DiscountDataMapper()

    DiscountDomainRepository discountDomainRepository = new DiscountDomainRepository(jpaDiscountRuleRepository, discountDataMapper)

    def "test findDiscountRule"() {

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
        def discountRule = discountDomainRepository.findDiscountRule()

        then:
        discountRule.range.productIds.get(0) == "123"
        discountRule.range.productIds.get(1) == "456"
        discountRule.conditions.get(0).getDiscount() == BigDecimal.valueOf(2.2)
        discountRule.conditions.get(0).getQuantity() == 20
        discountRule.conditions.get(1).getDiscount() == BigDecimal.valueOf(3.2)
        discountRule.conditions.get(1).getQuantity() == 10

    }

    def "should be discounted base on the product total price "() {

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
        def discountRule = discountDomainRepository.findDiscountRule()

        then:
        discountRule.range.productIds.get(0) == "123"
        discountRule.range.productIds.get(1) == "456"
        discountRule.conditions.get(0).getDiscount() == BigDecimal.valueOf(0.7)
        discountRule.conditions.get(0).getPrice() == BigDecimal.valueOf(500)
        discountRule.conditions.get(1).getDiscount() == BigDecimal.valueOf(0.8)
        discountRule.conditions.get(1).getPrice() == BigDecimal.valueOf(200)

    }
}
