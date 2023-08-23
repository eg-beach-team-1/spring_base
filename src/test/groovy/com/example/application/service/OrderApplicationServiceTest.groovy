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
import static com.example.domain.entity.ProductStatus.VALID
import static java.math.BigDecimal.valueOf

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
        OrderReqDto orderReqDto = new OrderReqDto(UUID.fromString("AC0E8B2C-4721-47FB-A784-92DC226FF84F"), orderProducts)

        List<Product> product = [new Product(PRODUCT_ID, "testProduct", BigDecimal.TEN, VALID, valueOf(0.8), 10)]
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
        OrderReqDto orderReqDto = new OrderReqDto(UUID.fromString("AC0E8B2C-4721-47FB-A784-92DC226FF84F"), orderProducts)

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
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: valueOf(10L), quantity: 2, discount: valueOf(0.8))]

        List<Order> orderDetails = [
                new Order(
                        id: "orderId1",
                        customerId: UUID.fromString("dcabcfac-6b08-47cd-883a-76c5dc366d88"),
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
        result[0].totalPrice == valueOf(20L)
        result[0].customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result[0].productDetails[0].priceDifference == valueOf(4L)
        result[0].productDetails[0].discountedPrice == valueOf(8L)
        result.size() == 1
    }

    @Shared
    List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: valueOf(10L), quantity: 2, discount: valueOf(0.8))]

    Order orderDetail = new Order(
            id: "8a4e94098a160b39018a160bd2f50000",
            customerId: UUID.fromString("dcabcfac-6b08-47cd-883a-76c5dc366d88"),
            status: CREATED,
            createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
            updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
            productDetails: productDetailList
    )

    def "should retrieve order by order id and customer id"() {
        given:
        orderRepository.findByOrderIdAndCustomerId(_, _) >> orderDetail

        when:
        def result = orderApplicationService.retrieveOrder("8a4e94098a160b39018a160bd2f50000", "dcabcfac-6b08-47cd-883a-76c5dc366d88")

        then:
        result.orderId == "8a4e94098a160b39018a160bd2f50000"
        result.totalPrice == valueOf(20L)
        result.customerId == "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        result.productDetails[0].priceDifference == valueOf(4L)
        result.productDetails[0].discountedPrice == valueOf(8L)
    }


    def "should update status when order is canceled"() {
        given:
        List<Product> productList = [new Product(id: 1, name: "water", price: valueOf(10L), status: VALID, discount: valueOf(0.8), stock: 10)]
        def orderId = "8a4e94098a160b39018a160bd2f50000"
        def customerId = "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        orderRepository.findByOrderIdAndCustomerId(_, _) >> orderDetail
        orderRepository.save(_) >> orderId
        productRepository.findAllById(_) >> productList

        when:
        def result = orderApplicationService.cancelOrder(orderId, customerId)

        then:
        result == orderId
    }

    def "should solve concurrency scenario successfully"() {
        given:
        Integer PRODUCT_ID = 1
        Integer QUANTITY = 1
        List<Product> products = [new Product(PRODUCT_ID, "testProduct", BigDecimal.TEN, VALID, valueOf(0.8), 1)]
        productRepository.findAllById(_) >> products
        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(PRODUCT_ID, QUANTITY))
        OrderReqDto orderReqDtoA = new OrderReqDto(UUID.randomUUID(), orderProducts)
        OrderReqDto orderReqDtoB = new OrderReqDto(UUID.randomUUID(), orderProducts)

        def orderService = new OrderApplicationService(productRepository,orderRepository)

        when:
        Thread customerAThread = new Thread({
            orderService.createOrder(orderReqDtoA)
        })
        Thread customerBThread = new Thread({
            orderService.createOrder(orderReqDtoB)
        })

        customerAThread.start()
        customerBThread.start()

        customerAThread.join()
        customerBThread.join()

        then:
        2 * productRepository.findAllById(List.of(PRODUCT_ID))

        and:
        def product = productRepository.findAllById(List.of(PRODUCT_ID)).stream().findFirst().orElse(null)
        if(Objects.nonNull(product)) {
            product.stock == 0
        }
    }

    def "should cancel order and add stock back when order is canceled"() {
        given:
        def orderId = "8a4e94098a160b39018a160bd2f50000"
        def customerId = "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        List<Product> productList = [new Product(id: 1, name: "water", price: valueOf(10L), status: VALID, discount: valueOf(0.8), stock: 10)]
        List<ProductDetail> productDetailList = [new ProductDetail(id: 1, name: "water", unitPrice: valueOf(10L), quantity: 2, discount: valueOf(0.8))]

        Order order = new Order(
                        id: orderId,
                        customerId: UUID.fromString(customerId),
                        status: CREATED,
                        createTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                        updateTime: LocalDateTime.of(2023, 8, 8, 10, 30, 0),
                        productDetails: productDetailList
                )


        orderRepository.findByOrderIdAndCustomerId(orderId, customerId) >> order
        productRepository.findAllById(List.of(1)) >> productList
        orderRepository.save(_) >> orderId

        when:
        def result = orderApplicationService.cancelOrder(orderId, customerId)

        then:
        result == orderId
        productList.get(0).stock == 12
    }
}