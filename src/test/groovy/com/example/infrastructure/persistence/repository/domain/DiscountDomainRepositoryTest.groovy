package com.example.infrastructure.persistence.repository.domain

import com.example.domain.entity.DiscountRule
import com.example.infrastructure.persistence.assembler.DiscountDataMapper
import com.example.infrastructure.persistence.entity.DiscountRulePo
import com.example.infrastructure.persistence.repository.JpaDiscountRuleRepository
import spock.lang.Specification

import static org.mockito.Mockito.when

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
}
