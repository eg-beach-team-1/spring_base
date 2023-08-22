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
        String customerId = "customer-id"
        OrderStatus status = OrderStatus.CREATED
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
        def product = new Product(1, "test", BigDecimal.ONE, VALID, BigDecimal.valueOf(0.8), 10, 1)
        def amount = 1

        when:
        def productDetail = OrderFactory.buildProductDetail(product, amount)

        then:
        productDetail.id == 1
        productDetail.name == "test"
        productDetail.unitPrice == BigDecimal.ONE
        productDetail.quantity == 1
        productDetail.discount == BigDecimal.valueOf(0.8)
    }

    def "should cancel the order success"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8) )]

        Order order = new Order(
                id: "8a4e94098a160b39018a160bd2f50000",
                customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
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
