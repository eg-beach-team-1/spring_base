package com.example.infrastructure.persistence.repository.domain

import com.example.domain.entity.Order
import com.example.domain.entity.OrderStatus
import com.example.domain.entity.ProductDetail
import com.example.domain.util.OrderUtils
import com.example.infrastructure.persistence.assembler.OrderProductDetailsDataMapper
import com.example.infrastructure.persistence.entity.OrderPo
import com.example.infrastructure.persistence.repository.JpaOrderRepository
import org.assertj.core.api.Assertions
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime

class OrderDomainRepositoryTest extends Specification {

    JpaOrderRepository jpaOrderRepository = Mock()
    ObjectMapper objectMapper = new ObjectMapper()
    OrderProductDetailsDataMapper orderProductDetailsDataMapper = new OrderProductDetailsDataMapper()
    OrderDomainRepository orderDomainRepository = new OrderDomainRepository(jpaOrderRepository, orderProductDetailsDataMapper)

    def "Should save order and return order Id"() {
        given:
        LocalDateTime createTime = LocalDateTime.now()
        LocalDateTime updateTime = LocalDateTime.now()
        String orderIdToSave = OrderUtils.generateOrderId()

        List<ProductDetail> productDetails = List.of(new ProductDetail(1, "productDetailName1", BigDecimal.ONE, 1))

        String productDetailsToSave = objectMapper.writeValueAsString(productDetails)

        Order orderToSave = new Order(orderIdToSave, "consumerId", OrderStatus.CREATED, createTime, updateTime, productDetails)

        OrderPo savedOrderPo = new OrderPo(orderIdToSave, "consumerId", BigDecimal.ONE, OrderStatus.CREATED, createTime, updateTime, productDetailsToSave)

        jpaOrderRepository.save(_) >> savedOrderPo

        when:
        def orderId = orderDomainRepository.save(orderToSave)

        then:
        Assertions.assertThat(orderId).usingRecursiveComparison().isEqualTo(orderIdToSave)
    }

    @Shared
    def jsonString =
            ''' [{
                         "id": 1,
                         "name": "water",
                         "price": 10,
                         "amount": 2
                     }]
                '''
    List<OrderPo> jpaOrdersList = [
            new OrderPo(
                    id: "546f4304-3be2-11ee-be56-0242ac120001",
                    customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
                    totalPrice: BigDecimal.valueOf(10L),
                    status: OrderStatus.CREATED,
                    createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                    updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                    productDetails: jsonString.toString()
            ),
            new OrderPo(
                    id: "546f4304-3be2-11ee-be56-0242ac120002",
                    customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
                    totalPrice: BigDecimal.valueOf(10L),
                    status: OrderStatus.CREATED,
                    createTime: LocalDateTime.of(2023, 8, 8, 11, 30, 0),
                    updateTime: LocalDateTime.of(2023, 8, 8, 11, 30, 0),
                    productDetails: jsonString.toString()
            ),
    ]

    def "should retrieve order list by customer id and order id"() {
        given:
        jpaOrderRepository.findByCustomerId("dcabcfac-6b08-47cd-883a-76c5dc366d88") >> jpaOrdersList

        when:
        def result = orderDomainRepository.findByCustomerIdAndOrderId("dcabcfac-6b08-47cd-883a-76c5dc366d88", "546f4304-3be2-11ee-be56-0242ac120001")

        then:
        result[0].id == "546f4304-3be2-11ee-be56-0242ac120001"
        result[0].customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result[0].status == OrderStatus.CREATED
        result[0].productDetails[0].id == 1
        result[0].productDetails[0].name == "water"
        result[0].productDetails[0].price == BigDecimal.valueOf(10L)
        result[0].productDetails[0].amount == 2
        result.size() == 1
    }

    def "should retrieve order list by customer id"() {
        given:
        jpaOrderRepository.findByCustomerId("dcabcfac-6b08-47cd-883a-76c5dc366d88") >> jpaOrdersList

        when:
        def result = orderDomainRepository.findByCustomerIdAndOrderId("dcabcfac-6b08-47cd-883a-76c5dc366d88", null)

        then:
        result[0].id == "546f4304-3be2-11ee-be56-0242ac120001"
        result[0].customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result[0].status == OrderStatus.CREATED
        result[0].productDetails[0].id == 1
        result[0].productDetails[0].name == "water"
        result[0].productDetails[0].price == BigDecimal.valueOf(10L)
        result[0].productDetails[0].amount == 2
        result[1].id == "546f4304-3be2-11ee-be56-0242ac120002"
        result.size() == 2
    }

}