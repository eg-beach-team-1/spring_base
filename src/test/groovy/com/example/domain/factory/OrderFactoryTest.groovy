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
        List<ProductDetail> productDetails = [new ProductDetail(id: 1, name: "water", price: BigDecimal.valueOf(10L), amount: 2)]

        when:
        def order = OrderFactory.buildOrder(customerId, status, productDetails)

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
        def product = new Product(1, "test", BigDecimal.ONE, VALID)
        def amount = 1L

        when:
        def productDetail = OrderFactory.buildProductDetail(product, amount)

        then:
        productDetail.id == 1
        productDetail.name == "test"
        productDetail.price == BigDecimal.ONE
        productDetail.amount == 1L
    }
}
