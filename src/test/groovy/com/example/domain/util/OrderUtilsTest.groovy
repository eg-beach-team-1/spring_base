package com.example.domain.util


import com.example.domain.entity.ProductDetail
import spock.lang.Specification

import static com.example.domain.util.OrderUtils.calculatePaidPrice
import static com.example.domain.util.OrderUtils.calculateTotalPrice

class OrderUtilsTest extends Specification {
    def "should calculate total price"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8)),
                                                 new ProductDetail(id: 2, name: "soda", unitPrice: BigDecimal.valueOf(10L), quantity: 1, discount: BigDecimal.valueOf(0.8))]

        when:
        def result = calculateTotalPrice(productDetailList)

        then:
        result == BigDecimal.valueOf(30L)
    }

    def "should calculate paid price"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8)),
                                                 new ProductDetail(id: 2, name: "soda", unitPrice: BigDecimal.valueOf(10L), quantity: 1, discount: BigDecimal.valueOf(0.8))]
        when:
        def result = calculatePaidPrice(productDetailList)

        then:
        result == BigDecimal.valueOf(24L)
    }
}
