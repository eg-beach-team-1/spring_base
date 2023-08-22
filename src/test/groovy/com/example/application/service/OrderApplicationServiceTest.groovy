package com.example.application.service

import com.example.common.exception.BusinessException
import com.example.domain.entity.*
import com.example.domain.repository.OrderRepository
import com.example.domain.repository.ProductRepository
import com.example.domain.util.OrderUtils
import com.example.presentation.vo.request.OrderProductReqDto
import com.example.presentation.vo.request.OrderReqDto
import org.assertj.core.api.Assertions
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime

import static com.example.domain.entity.OrderStatus.CREATED

class OrderApplicationServiceTest extends Specification {

    ProductRepository productRepository = Mock()
    OrderRepository orderRepository = Mock()
    OrderApplicationService orderApplicationService = new OrderApplicationService(productRepository, orderRepository)

    def "should save order whose all products are valid and return correct order id"() {
        given:
        Integer PRODUCT_ID = 11
        String ORDER_ID = OrderUtils.generateOrderId()
        Integer QUANTITY = 10

        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(PRODUCT_ID, QUANTITY))
        OrderReqDto orderReqDto = new OrderReqDto("customerId", orderProducts)

        List<Product> product = [new Product(PRODUCT_ID, "testProduct", BigDecimal.TEN, ProductStatus.VALID, BigDecimal.valueOf(0.8), 10, 1)]
        productRepository.findAllById(_) >> product

        orderRepository.save(_) >> ORDER_ID

        productRepository.findById(_) >> product.get(0)

        when:
        String result = orderApplicationService.createOrder(orderReqDto)

        then:
        Assertions.assertThat(result.equals(ORDER_ID))
    }

    def "should throw exception given some products not in repo"() {
        given:
        Integer PRODUCT_ID = 11
        String ORDER_ID = OrderUtils.generateOrderId()
        Integer QUANTITY = 10

        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(PRODUCT_ID, QUANTITY))
        OrderReqDto orderReqDto = new OrderReqDto("customerId", orderProducts)

        List<Product> product = List.of()
        productRepository.findAllById(_) >> product

        orderRepository.save(_) >> ORDER_ID

        when:
        orderApplicationService.createOrder(orderReqDto)

        then:
        thrown(BusinessException)
    }

    def "should retrieve orders by customer id"() {
        given:
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8))]

        List<Order> orderDetails = [
                new Order(
                        id: "orderId1",
                        customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
                        status: OrderStatus.CREATED,
                        createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                        updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                        productDetails: productDetailList
                )
        ]

        orderRepository.findByCustomerId(_) >> orderDetails


        when:
        def result = orderApplicationService.retrieveOrders("dcabcfac-6b08-47cd-883a-76c5dc366d88")

        then:
        result[0].orderId == "orderId1"
        result[0].totalPrice == BigDecimal.valueOf(20L)
        result[0].customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result[0].productDetails[0].priceDifference == BigDecimal.valueOf(4L)
        result[0].productDetails[0].discountedPrice == BigDecimal.valueOf(8L)
        result.size() == 1
    }

    @Shared
    List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: BigDecimal.valueOf(10L), quantity: 2, discount: BigDecimal.valueOf(0.8))]

    Order orderDetail = new Order(
            id: "8a4e94098a160b39018a160bd2f50000",
            customerId: "dcabcfac-6b08-47cd-883a-76c5dc366d88",
            status: CREATED,
            createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
            updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
            productDetails: productDetailList
    )

    def "should retrieve order by order id"() {
        given:
        orderRepository.findByOrderId(_) >> orderDetail

        when:
        def result = orderApplicationService.retrieveOrder("8a4e94098a160b39018a160bd2f50000")

        then:
        result.orderId == "8a4e94098a160b39018a160bd2f50000"
        result.totalPrice == BigDecimal.valueOf(20L)
        result.customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result.productDetails[0].priceDifference == BigDecimal.valueOf(4L)
        result.productDetails[0].discountedPrice == BigDecimal.valueOf(8L)
    }

    def "should throw business exception when version is different and stock is 1"() {
        given:
        Integer PRODUCT_ID = 11
        String ORDER_ID = OrderUtils.generateOrderId()
        Integer QUANTITY = 1

        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(PRODUCT_ID, QUANTITY))
        OrderReqDto orderReqDto = new OrderReqDto("customerId", orderProducts)

        List<Product> product = [new Product(PRODUCT_ID, "testProduct", BigDecimal.TEN, ProductStatus.VALID, BigDecimal.valueOf(0.8), 1, 1)]

        def productInDB = new Product(PRODUCT_ID, "testProduct", BigDecimal.TEN, ProductStatus.VALID, BigDecimal.valueOf(0.8), 1, 2)


        productRepository.findAllById(_) >> product

        orderRepository.save(_) >> ORDER_ID

        productRepository.findById(_) >> productInDB

        when:
        orderApplicationService.createOrder(orderReqDto)

        then:
        thrown(BusinessException)
    }

    def "should update status when order is canceled"() {
        given:
        def orderId = "8a4e94098a160b39018a160bd2f50000"
        def customerId = "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        orderRepository.findByOrderIdAndCustomerId(_, _) >> orderDetail
        orderRepository.save(_) >> orderId

        when:
        def result = orderApplicationService.cancelOrder(orderId, customerId)

        then:
        result == orderId
    }
}