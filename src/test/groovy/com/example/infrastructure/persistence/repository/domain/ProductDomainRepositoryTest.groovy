package com.example.infrastructure.persistence.repository.domain

import com.example.common.exception.NotFoundException
import com.example.domain.entity.Product
import com.example.infrastructure.persistence.entity.ProductPo
import com.example.infrastructure.persistence.repository.JpaProductRepository
import org.assertj.core.api.Assertions
import spock.lang.Specification

import static com.example.domain.entity.ProductStatus.VALID
import static java.lang.Enum.valueOf
import static java.math.BigDecimal.valueOf

class ProductDomainRepositoryTest extends Specification {
    JpaProductRepository jpaProductRepository = Stub()
    ProductDomainRepository productDomainRepository = new ProductDomainRepository(jpaProductRepository)

    def "should return all products"() {
        given:
        List<ProductPo> jpaProducts = [
                new ProductPo(id: 1, name: "book", price: valueOf(10L), status: "VALID", discount: BigDecimal.ONE, version: valueOf(1L)),
                new ProductPo(id: 2, name: "book2", price: valueOf(10L), status: "INVALID", discount: valueOf(0.8), version: valueOf(1L)),
                new ProductPo(id: 3, name: "book2", price: null, status: "VALID", discount: valueOf(0.5), version: valueOf(1L)),
        ]

        jpaProductRepository.findAll() >> jpaProducts

        List<Product> expectedProducts = [
                new Product(id: 1, name: "book", price: valueOf(10L), status: "VALID", discount: BigDecimal.ONE, version: valueOf(1L)),
                new Product(id: 2, name: "book2", price: valueOf(10L), status: "INVALID", discount: valueOf(0.8), version: valueOf(1L)),
                new Product(id: 3, name: "book2", price: null, status: "VALID", discount: valueOf(0.5), version: valueOf(1L)),
        ]

        when:
        def result = productDomainRepository.findAll()

        then:
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedProducts)
    }

    def "should return correct product"() {
        given:
        ProductPo jpaProduct = new ProductPo(id: 1, name: "book", price: valueOf(10L), status: "VALID", discount: valueOf(0.5), version: valueOf(1L))

        jpaProductRepository.findById(1) >> Optional.of(jpaProduct)

        Product expectedProduct = new Product(id: 1, name: "book", price: valueOf(10L), status: "VALID", discount: valueOf(0.5), version: valueOf(1L))

        when:
        def result = productDomainRepository.findById(1)

        then:
        Assertions.assertThat(result == expectedProduct)
    }


    def "should throw exception given not exist product id"() {
        given:
        jpaProductRepository.findById(2) >> Optional.empty()

        when:
        productDomainRepository.findById(2)

        then:
        thrown(NotFoundException)
    }

    def "should find all products by ids"() {
        given:
        List<ProductPo> jpaProducts = [
                new ProductPo(id: 1, name: "book", price: valueOf(10L), status: "VALID", discount: BigDecimal.ONE, version: valueOf(1L)),
                new ProductPo(id: 2, name: "book2", price: valueOf(10L), status: "INVALID", discount: valueOf(0.8), version: valueOf(1L)),
                new ProductPo(id: 3, name: "book2", price: null, status: "VALID", discount: BigDecimal.ONE, version: valueOf(1L)),
        ]

        jpaProductRepository.findAllById(_ as Iterable<Integer>) >> jpaProducts

        List<Product> expectedProducts = [
                new ProductPo(id: 1, name: "book", price: valueOf(10L), status: "VALID", version: valueOf(1L)),
                new ProductPo(id: 2, name: "book2", price: valueOf(10L), status: "INVALID", discount: valueOf(0.8), version: valueOf(1L)),
                new ProductPo(id: 3, name: "book2", price: null, status: "VALID", discount: BigDecimal.ONE, version: valueOf(1L)),
        ] as List<Product>

        when:
        def result = productDomainRepository.findAllById(List.of(1, 2, 3))

        then:
        Assertions.assertThat(result == expectedProducts)
    }

    def "should save product successfully"() {
        given:
        def product = new Product(id: 1, name: "book", price: valueOf(10L), status: VALID, discount: BigDecimal.ONE, version: valueOf(1L))

        when:
        productDomainRepository.save(product)

        then:
        noExceptionThrown()
    }


}
