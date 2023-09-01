package com.example.domain.factory

import com.example.domain.entity.Order
import com.example.domain.entity.OrderStatus
import com.example.domain.entity.Product
import com.example.domain.entity.ProductDetail
import spock.lang.Specification

import java.time.LocalDateTime

import static com.example.domain.entity.OrderStatus.CANCELED
import static com.example.domain.entity.OrderStatus.CREATED
import static com.example.domain.entity.ProductStatus.VALID

class OrderFactoryTest extends Specification {
    def "should create order successfully"() {
        given:
        UUID customerId = UUID.fromString("9B9FD364-8C65-48B0-9172-7A52C991E18A")
        OrderStatus status = CREATED
        List<ProductDetail> productDetails = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8) )]

        when:
        def order = OrderFactory.buildOrder(customerId, productDetails)

        then:
        order.getId() != null
        order.getCustomerId() == customerId
        order.getStatus() == status
        order.getCreateTime() != null
        order.getUpdateTime() != null
        order.getProductDetails() == productDetails
    }

    def "should build product successfully"() {
        given:
        def product = new Product(1, "test", BigDecimal.ONE, VALID, "clothes", BigDecimal.valueOf(0.8), 10)
        def amount = 1

        when:
        def productDetail = OrderFactory.buildProductDetail(product, amount)

        then:
        productDetail.id == 1
        productDetail.name == "test"
        productDetail.unitPrice == BigDecimal.ONE
        productDetail.category == "clothes"
        productDetail.quantity == 1
        productDetail.discount == BigDecimal.valueOf(0.8)
    }

    def "should cancel the order success"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), category: "drink", quantity: 2, discount: BigDecimal.valueOf(0.8) )]

        Order order = new Order(
                id: "8a4e94098a160b39018a160bd2f50000",
                customerId: UUID.fromString("dcabcfac-6b08-47cd-883a-76c5dc366d88"),
                status: CREATED,
                createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                productDetails: productDetailList
        )

        when:
        OrderFactory.cancelOrder(order)

        then:
        order.status == CANCELED
    }
}
