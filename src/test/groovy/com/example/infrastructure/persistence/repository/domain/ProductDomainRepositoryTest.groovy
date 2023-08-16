package com.example.infrastructure.persistence.repository.domain

import com.example.common.exception.NotFoundException
import com.example.domain.entity.Product
import com.example.domain.repository.ProductRepository
import com.example.infrastructure.persistence.entity.ProductPo
import com.example.infrastructure.persistence.repository.JpaProductRepository
import org.assertj.core.api.Assertions
import spock.lang.Specification

class ProductDomainRepositoryTest extends Specification {
    JpaProductRepository jpaProductRepository = Mock()
    ProductDomainRepository productDomainRepository = new ProductDomainRepository(jpaProductRepository)

    def "should return all products"() {
        given:
        List<ProductPo> jpaProducts = [
                new ProductPo(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID", discount: BigDecimal.ONE),
                new ProductPo(id: 2, name: "book2", price: BigDecimal.valueOf(10L), status: "INVALID", discount: BigDecimal.valueOf(0.8)),
                new ProductPo(id: 3, name: "book2", price: null, status: "VALID", discount: BigDecimal.valueOf(0.5)),
        ]

        jpaProductRepository.findAll() >> jpaProducts

        List<Product> expectedProducts = [
                new Product(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID", discount: BigDecimal.ONE),
                new Product(id: 2, name: "book2", price: BigDecimal.valueOf(10L), status: "INVALID", discount: BigDecimal.valueOf(0.8) ),
                new Product(id: 3, name: "book2", price: null, status: "VALID", discount: BigDecimal.valueOf(0.5)),
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
        ProductPo jpaProduct = new ProductPo(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID", discount: BigDecimal.valueOf(0.5))

        jpaProductRepository.findById(1) >> Optional.of(jpaProduct)

        Product expectedProduct = new Product(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID", discount: BigDecimal.valueOf(0.5))

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
                new ProductPo(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID", discount: BigDecimal.ONE),
                new ProductPo(id: 2, name: "book2", price: BigDecimal.valueOf(10L), status: "INVALID", discount: BigDecimal.valueOf(0.8)),
                new ProductPo(id: 3, name: "book2", price: null, status: "VALID", discount: BigDecimal.ONE),
        ]

        jpaProductRepository.findAllById(_ as Iterable<Integer>) >> jpaProducts

        List<Product> expectedProducts = [
                new ProductPo(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID"),
                new ProductPo(id: 2, name: "book2", price: BigDecimal.valueOf(10L), status: "INVALID", discount: BigDecimal.valueOf(0.8)),
                new ProductPo(id: 3, name: "book2", price: null, status: "VALID", discount: BigDecimal.ONE),
        ] as List<Product>

        when:
        def result = productDomainRepository.findAllById(List.of(1, 2, 3))

        then:
        Assertions.assertThat(result == expectedProducts)

    }


}
