package com.example.application.service

import com.example.domain.entity.Customer
import com.example.domain.repository.CustomerRepository
import com.example.presentation.vo.response.CustomerDto
import org.assertj.core.api.Assertions
import spock.lang.Specification

class CustomerApplicationServiceTest extends Specification {

    CustomerRepository customerRepository = Mock()
    CustomerApplicationService customerApplicationService = new CustomerApplicationService(customerRepository)

    def "should retrieve customer given id"() {
        def customerId = "1"
        given:
        Customer customerFromRepo = new Customer(id: customerId, name: "client1")

        customerRepository.findById(customerId) >> customerFromRepo

        CustomerDto expectedCustomer = new CustomerDto(id: customerId, name: "client1")

        when:
        def result = customerApplicationService.findById(customerId)

        then:
        result.properties == expectedCustomer.properties
    }
}
