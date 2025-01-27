package com.example.domain.entity

import com.example.common.exception.BusinessException
import spock.lang.Specification

class ProductTest extends Specification {
    def "should not thrown exception when product is valid"() {
        given:
        def product = new Product(1, "name", BigDecimal.ONE, ProductStatus.VALID, "clothes", BigDecimal.valueOf(0.8), 10)

        when:
        product.validateProduct()

        then:
        noExceptionThrown()
    }

    def "should thrown exception when product is invalid"() {
        given:
        def product = new Product(1, "name", BigDecimal.ONE, ProductStatus.INVALID, "clothes", BigDecimal.valueOf(0.8), 10)

        when:
        product.validateProduct()

        then:
        thrown(BusinessException)
    }

    def "should thrown exception when product does not have a proper price"() {
        given:
        def product = new Product(1, "name", null, ProductStatus.VALID, "clothes", BigDecimal.valueOf(0.8), 10)

        when:
        product.validateProduct()

        then:
        thrown(BusinessException)
    }

    def "should calculate discounted price successfully"() {
        given:
        def product = new Product(1, "name", BigDecimal.TEN, ProductStatus.INVALID, "clothes", BigDecimal.valueOf(0.8), 10)

        when:
        def discountedPrice = product.calculateDiscountedPrice()

        then:
        discountedPrice == BigDecimal.valueOf(8)
    }

    def "should calculate discounted price rounded by half successfully"() {
        given:
        def product = new Product(1, "name", BigDecimal.valueOf(23.3), ProductStatus.INVALID, "clothes", BigDecimal.valueOf(0.65), 10)

        when:
        def discountedPrice = product.calculateDiscountedPrice()

        then:
        discountedPrice == BigDecimal.valueOf(15.15)
    }

    def "should calculate discounted price minimum as 0.01 successfully"() {
        given:
        def product = new Product(1, "name", BigDecimal.valueOf(0.5), ProductStatus.INVALID, "clothes", BigDecimal.valueOf(0.01), 10)

        when:
        def discountedPrice = product.calculateDiscountedPrice()

        then:
        discountedPrice == BigDecimal.valueOf(0.01)
    }

    def "should return null discounted price when the price of product is null"() {
        given:
        def product = new Product(1, "name", null, ProductStatus.INVALID, "clothes", BigDecimal.valueOf(0.8), 10)

        when:
        def discountedPrice = product.calculateDiscountedPrice()

        then:
        discountedPrice == null
    }

    def "should consume product successfully when stock is enough"() {
        given:
        def product = new Product(1, "name", null, ProductStatus.VALID, "clothes", BigDecimal.valueOf(0.8), 10)

        when:
        product.consume(5)

        then:
        noExceptionThrown()
    }

    def "should thrown business exception when stock is zero"() {
        given:
        def product = new Product(1, "name", null, ProductStatus.VALID, "clothes", BigDecimal.valueOf(0.8), 0)

        when:
        product.validateStock(1)

        then:
        thrown(BusinessException)
    }

    def "should thrown business exception when stock is less than amount"() {
        given:
        def product = new Product(1, "name", null, ProductStatus.VALID, "clothes", BigDecimal.valueOf(0.8), 1)

        when:
        product.validateStock(2)

        then:
        thrown(BusinessException)
    }

    def "should add stock when order is canceled"() {
        given:
        def product = new Product(1, "name", null, ProductStatus.VALID, "clothes", BigDecimal.valueOf(0.8), 1)

        when:
        product.addStock(5)

        then:
        product.stock == 6
    }


}
