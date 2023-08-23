package com.example.infrastructure.persistence.repository.domain

import com.example.common.exception.BusinessException
import com.example.infrastructure.persistence.entity.CustomerPo
import com.example.infrastructure.persistence.repository.JpaCustomerRepository
import spock.lang.Specification

class CustomerDomainRepositoryTest extends Specification {

    JpaCustomerRepository jpaCustomerRepository = Mock()
    CustomerDomainRepository customerDomainRepository = new CustomerDomainRepository(jpaCustomerRepository)

    def "should_find_customer_by_id_successfully"() {
        given:

        def clientId = "1"
        CustomerPo singleCustomerPo = new CustomerPo(id: clientId, name: "client1")

        jpaCustomerRepository.findById(clientId) >> Optional.of(singleCustomerPo)

        when:
        def result = customerDomainRepository.findById(clientId)

        then:
        result.id == singleCustomerPo.id
        result.name == singleCustomerPo.name
    }

    def "should_throw_exception_when_customer_not_found"() {
        given:

        def clientId = "1"
        jpaCustomerRepository.findById(clientId) >> Optional.empty()

        when:
        customerDomainRepository.findById(clientId)

        then:
        thrown(BusinessException)
    }
}
