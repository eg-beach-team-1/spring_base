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
                new Product(id: 1, name: "book", price: BigDecimal.valueOf(10.58F), status: "VALID", discount: BigDecimal.valueOf(0.4), stock: 1),
                new Product(id: 2, name: "book2", price: BigDecimal.valueOf(10.55F), status: "INVALID", discount: BigDecimal.valueOf(0.5), stock: 1),
                new Product(id: 3, name: "book3", price: null, status: "VALID", discount: BigDecimal.valueOf(0.8), stock: 1),
                new Product(id: 4, name: "book4", price: BigDecimal.valueOf(0.01F), status: "VALID", discount: BigDecimal.valueOf(0.8), stock: 1),
        ]

        productRepository.findAll() >> productList

        List<ProductDto> expectedProductList = [
                new ProductDto(id: 1, name: "book", price: BigDecimal.valueOf(10.58F), status: "VALID", discount: BigDecimal.valueOf(0.4), discountedPrice: BigDecimal.valueOf(4.23), stock: 1),
                new ProductDto(id: 2, name: "book2", price: BigDecimal.valueOf(10.55F), status: "INVALID", discount: BigDecimal.valueOf(0.5), discountedPrice: BigDecimal.valueOf(5.28), stock: 1),
                new ProductDto(id: 3, name: "book3", price: null, status: "VALID", discount: BigDecimal.valueOf(0.8), discountedPrice: null, stock: 1),
                new ProductDto(id: 4, name: "book4", price: BigDecimal.valueOf(0.01F), status: "VALID", discount: BigDecimal.valueOf(0.8), discountedPrice: BigDecimal.valueOf(0.01),stock: 1),
        ]

        when:
        def result = productApplicationService.findAll()

        then:
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expectedProductList)

    }
}
