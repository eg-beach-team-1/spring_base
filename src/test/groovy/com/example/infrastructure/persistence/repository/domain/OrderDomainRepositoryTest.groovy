package com.example.infrastructure.persistence.repository.domain

import com.example.domain.entity.Order
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

import static com.example.domain.entity.OrderStatus.CREATED
import static java.math.BigDecimal.valueOf

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

        List<ProductDetail> productDetails = List.of(new ProductDetail(1, "productDetailName1", BigDecimal.TEN, 1, valueOf(0.8)))

        String productDetailsToSave = objectMapper.writeValueAsString(productDetails)

        Order orderToSave = new Order(orderIdToSave, "consumerId", CREATED, createTime, updateTime, productDetails)

        OrderPo savedOrderPo = new OrderPo(orderIdToSave, "consumerId", CREATED, createTime, updateTime, productDetailsToSave)

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
                         "unitPrice": 10,
                         "quantity": 2,
                         "discount": 0.8
                     }]
                '''
    List<OrderPo> jpaOrdersList = [
            new OrderPo(
                    id: "546f4304-3be2-11ee-be56-0242ac120001",
                    customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
                    status: CREATED,
                    createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                    updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                    productDetails: jsonString.toString()
            ),
            new OrderPo(
                    id: "546f4304-3be2-11ee-be56-0242ac120002",
                    customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
                    status: CREATED,
                    createTime: LocalDateTime.of(2023, 8, 8, 11, 30, 0),
                    updateTime: LocalDateTime.of(2023, 8, 8, 11, 30, 0),
                    productDetails: jsonString.toString()
            ),
    ]

    OrderPo jpaOrder = new OrderPo(
            id: "546f4304-3be2-11ee-be56-0242ac120001",
            customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
            status: CREATED,
            createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
            updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
            productDetails: jsonString.toString()
    )

    def "should retrieve orders by customer id "() {
        given:
        jpaOrderRepository.findByCustomerId("dcabcfac-6b08-47cd-883a-76c5dc366d88") >> jpaOrdersList

        when:
        def result = orderDomainRepository.findByCustomerId("dcabcfac-6b08-47cd-883a-76c5dc366d88")

        then:
        result[0].id == "546f4304-3be2-11ee-be56-0242ac120001"
        result[0].customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result[0].status == CREATED
        result[0].productDetails[0].id == 1
        result[0].productDetails[0].name == "water"
        result[0].productDetails[0].unitPrice == valueOf(10L)
        result[0].productDetails[0].quantity == 2
        result[0].productDetails[0].discount == valueOf(0.8)
        result[1].id == "546f4304-3be2-11ee-be56-0242ac120002"
        result[1].customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result[1].status == CREATED
        result[1].productDetails[0].id == 1
        result[1].productDetails[0].name == "water"
        result[1].productDetails[0].unitPrice == valueOf(10L)
        result[1].productDetails[0].quantity == 2
        result[1].productDetails[0].discount == valueOf(0.8)
        result.size() == 2
    }

    def "should retrieve order by order id"() {
        given:
        jpaOrderRepository.findById(_) >> Optional.of(jpaOrder
        )

        when:
        def result = orderDomainRepository.findByOrderId("546f4304-3be2-11ee-be56-0242ac120001")

        then:
        result.id == "546f4304-3be2-11ee-be56-0242ac120001"
        result.customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result.status == CREATED
        result.productDetails[0].id == 1
        result.productDetails[0].name == "water"
        result.productDetails[0].unitPrice == valueOf(10L)
        result.productDetails[0].quantity == 2
        result.productDetails[0].discount == valueOf(0.8)
    }

    def "should retrieve order by order id and customer id"() {
        given:
        jpaOrderRepository.findByIdAndCustomerId(_, _) >> Optional.of(jpaOrder
        )
        def orderId = "546f4304-3be2-11ee-be56-0242ac120001"
        def customerId = "dcabcfac-6b08-47cd-883a-76c5dc366d88"

        when:
        def result = orderDomainRepository.findByOrderIdAndCustomerId(orderId, customerId)

        then:
        result.id == orderId
        result.customerId == customerId
        result.status == CREATED
        result.productDetails[0].id == 1
        result.productDetails[0].name == "water"
        result.productDetails[0].unitPrice == valueOf(10L)
        result.productDetails[0].quantity == 2
        result.productDetails[0].discount == valueOf(0.8)
    }
}
