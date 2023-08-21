package com.example.domain.factory

import com.example.domain.entity.OrderStatus
import com.example.domain.entity.Product
import com.example.domain.entity.ProductDetail
import spock.lang.Specification

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
}
