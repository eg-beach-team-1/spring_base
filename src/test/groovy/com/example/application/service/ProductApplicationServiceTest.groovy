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
                new Product(id: 1, name: "book", price: BigDecimal.valueOf(10.58F), status: "VALID", discount: BigDecimal.valueOf(0.4)),
                new Product(id: 2, name: "book2", price: BigDecimal.valueOf(10.55F), status: "INVALID", discount: BigDecimal.valueOf(0.5)),
                new Product(id: 3, name: "book2", price: null, status: "VALID", discount: BigDecimal.valueOf(0.8)),
        ]

        productRepository.findAll() >> productList

        List<ProductDto> expectedProductList = [
                new ProductDto(id: 1, name: "book", price: BigDecimal.valueOf(10L), status: "VALID", discount: BigDecimal.ONE, discountedPrice: BigDecimal.valueOf(4.23)),
                new ProductDto(id: 2, name: "book2", price: BigDecimal.valueOf(10L), status: "INVALID", discount: BigDecimal.valueOf(0.5), discountedPrice: BigDecimal.valueOf(5.28)),
                new ProductDto(id: 3, name: "book2", price: null, status: "VALID", discount: BigDecimal.valueOf(0.8), discountedPrice: null),
        ] as List<ProductDto>

        when:
        def result = productApplicationService.findAll()

        then:
        Assertions.assertThat(result == expectedProductList)

    }
}
