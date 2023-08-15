package com.example.application.service

import com.example.domain.entity.Product
import com.example.domain.repository.ProductRepository
import com.example.presentation.vo.response.ProductDto
import org.assertj.core.api.Assertions
import spock.lang.Specification

class ProductApplicationServiceTest extends Specification {

    ProductRepository productRepository = Mock()
    ProductApplicationService productApplicationService = new ProductApplicationService(productRepository)

    def "should return all products"() {
        given:
        List<Product> productList = [
                new Product(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID"),
                new Product(id: 2, name: "book2", price: BigDecimal.valueOf(10L), status: "INVALID"),
                new Product(id: 3, name: "book2", price: null, status: "VALID"),
        ]

        productRepository.findAll() >> productList

        List<ProductDto> expectedProductList = [
                new ProductDto(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID"),
                new ProductDto(id: 2, name: "book2", price: BigDecimal.valueOf(10L), status: "INVALID"),
                new ProductDto(id: 3, name: "book2", price: null, status: "VALID"),
        ]

        when:
        def result = productApplicationService.findAll()

        then:
        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedProductList)
    }
}
