package com.example.infrastructure.persistence.repository.domain

import com.example.common.exception.BusinessException
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

    def "Should save order and return order"() {
        given:
        LocalDateTime createTime = LocalDateTime.now()
        LocalDateTime updateTime = LocalDateTime.now()
        String orderIdToSave = OrderUtils.generateOrderId()

        List<ProductDetail> productDetails = List.of(new ProductDetail(1, "productDetailName1", BigDecimal.TEN, "clothes", 1, valueOf(0.8)))

        String productDetailsToSave = objectMapper.writeValueAsString(productDetails)

        OrderPo orderPoSaved = new OrderPo(orderIdToSave, "464547B0-C850-4238-BD23-1A30383CBE84", CREATED, createTime, updateTime, productDetailsToSave)
        jpaOrderRepository.save(_) >> orderPoSaved

        Order orderToSave = new Order(orderIdToSave, UUID.fromString("464547B0-C850-4238-BD23-1A30383CBE84"), CREATED, createTime, updateTime, productDetails)

        when:
        def order = orderDomainRepository.save(orderToSave)

        then:
        Assertions.assertThat(order).usingRecursiveComparison().isEqualTo(orderToSave)
    }

    @Shared
    def jsonString =
            ''' [{
                         "id": 1,
                         "name": "water",
                         "unitPrice": 10,
                         "category": "drink",
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
        result[0].customerId == UUID.fromString("dcabcfac-6b08-47cd-883a-76c5dc366d88")
        result[0].status == CREATED
        result[0].productDetails[0].id == 1
        result[0].productDetails[0].name == "water"
        result[0].productDetails[0].unitPrice == valueOf(10L)
        result[0].productDetails[0].category == "drink"
        result[0].productDetails[0].quantity == 2
        result[0].productDetails[0].discount == valueOf(0.8)
        result[1].id == "546f4304-3be2-11ee-be56-0242ac120002"
        result[1].customerId == UUID.fromString("dcabcfac-6b08-47cd-883a-76c5dc366d88")
        result[1].status == CREATED
        result[1].productDetails[0].id == 1
        result[1].productDetails[0].name == "water"
        result[1].productDetails[0].unitPrice == valueOf(10L)
        result[1].productDetails[0].category == "drink"
        result[1].productDetails[0].quantity == 2
        result[1].productDetails[0].discount == valueOf(0.8)
        result.size() == 2
    }

    def "should retrieve order by order id"() {
        given:
        jpaOrderRepository.findByIdAndCustomerId(_, _) >> Optional.of(jpaOrder)

        when:
        def result = orderDomainRepository.findByOrderIdAndCustomerId("546f4304-3be2-11ee-be56-0242ac120001", "dcabcfac-6b08-47cd-883a-76c5dc366d88")

        then:
        result.id == "546f4304-3be2-11ee-be56-0242ac120001"
        result.customerId == UUID.fromString("dcabcfac-6b08-47cd-883a-76c5dc366d88")
        result.status == CREATED
        result.productDetails[0].id == 1
        result.productDetails[0].name == "water"
        result.productDetails[0].unitPrice == valueOf(10L)
        result.productDetails[0].category == "drink"
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
        result.customerId == UUID.fromString(customerId)
        result.status == CREATED
        result.productDetails[0].id == 1
        result.productDetails[0].name == "water"
        result.productDetails[0].unitPrice == valueOf(10L)
        result.productDetails[0].category == "drink"
        result.productDetails[0].quantity == 2
        result.productDetails[0].discount == valueOf(0.8)
    }

    def "should throw exception when can not retrieve order"() {
        given:
        def orderId = "546f4304-3be2-11ee-be56-0242ac120001"
        def customerId = "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        jpaOrderRepository.findByIdAndCustomerId(_, _) >> Optional.empty()

        when:
        orderDomainRepository.findByOrderIdAndCustomerId(orderId, customerId)

        then:
        thrown(BusinessException)
    }
}
