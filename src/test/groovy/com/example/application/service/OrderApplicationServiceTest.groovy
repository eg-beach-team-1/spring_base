package com.example.application.service

import com.example.common.exception.BusinessException
import com.example.domain.entity.*
import com.example.domain.factory.OrderFactory
import com.example.domain.port.OrderDataServiceClient
import com.example.domain.repository.DiscountRepository
import com.example.domain.repository.OrderRepository
import com.example.domain.repository.ProductRepository
import com.example.domain.util.OrderUtils
import com.example.presentation.vo.request.OrderProductReqDto
import com.example.presentation.vo.request.OrderReqDto
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime

import static com.example.domain.entity.OrderStatus.CANCELED
import static com.example.domain.entity.OrderStatus.CREATED
import static com.example.domain.entity.ProductStatus.VALID
import static java.math.BigDecimal.valueOf

class OrderApplicationServiceTest extends Specification {

    ProductRepository productRepository = Mock()
    OrderRepository orderRepository = Mock()
    DiscountRepository discountRepository = Mock()
    OrderDataServiceClient orderDataServiceClient = Mock()

    OrderApplicationService orderApplicationService = new OrderApplicationService(productRepository, orderRepository, discountRepository, orderDataServiceClient)

    def "should save order whose all products are valid and return correct order id"() {
        given:
        Integer PRODUCT_ID = 11
        Integer QUANTITY = 10
        String CATEGORY = "clothes"

        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(PRODUCT_ID, QUANTITY))
        OrderReqDto orderReqDto = new OrderReqDto(UUID.fromString("AC0E8B2C-4721-47FB-A784-92DC226FF84F"), orderProducts)

        List<Product> product = [new Product(PRODUCT_ID, "testProduct", BigDecimal.TEN, VALID, CATEGORY, valueOf(0.8), 10)]
        productRepository.findAllById(_) >> product

        List<ProductDetail> productDetailList = [new ProductDetail(PRODUCT_ID, "testProduct", BigDecimal.TEN, CATEGORY, 10, valueOf(0.8))]
        Order orderSaved = OrderFactory.buildOrder(UUID.fromString("ac0e8b2c-4721-47fb-a784-92dc226ff84f"), productDetailList)
        orderRepository.save(_) >> orderSaved

        productRepository.findById(_) >> product.get(0)

        when:
        String result = orderApplicationService.createOrder(orderReqDto)

        then:
        result == orderSaved.getId()
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
                        status: CREATED,
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
        result[0].productDetails[0].discountedPrice == valueOf(16L)
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

    Order orderCancelled = new Order(
            id: "8a4e94098a160b39018a160bd2f50000",
            customerId: UUID.fromString("dcabcfac-6b08-47cd-883a-76c5dc366d88"),
            status: CANCELED,
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
        result.productDetails[0].discountedPrice == valueOf(16L)
    }


    def "should update status when order is canceled"() {
        given:
        List<Product> productList = [new Product(id: 1, name: "water", price: valueOf(10L), status: VALID, discount: valueOf(0.8), stock: 10)]
        def orderId = "8a4e94098a160b39018a160bd2f50000"
        def customerId = "dcabcfac-6b08-47cd-883a-76c5dc366d88"
        orderRepository.findByOrderIdAndCustomerId(_, _) >> orderDetail
        orderRepository.save(_) >> orderCancelled
        productRepository.findAllById(_) >> productList

        when:
        def result = orderApplicationService.cancelOrder(orderId, customerId)

        then:
        result == orderCancelled.getId()
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
        orderRepository.save(_) >> orderCancelled

        when:
        def result = orderApplicationService.cancelOrder(orderId, customerId)

        then:
        result == orderCancelled.getId()
        productList.get(0).stock == 12
    }

    def "should preview order detail"() {
        Range mockRange = Mock()
        Condition mockCondition = Mock()

        given:
        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(1, 3))
        OrderReqDto orderReqDto = new OrderReqDto(UUID.fromString("AC0E8B2C-4721-47FB-A784-92DC226FF84F"), orderProducts)

        DiscountRule discountRule = new DiscountRule(range: mockRange, conditions: [mockCondition])

        def product1 = new Product(1, "testProduct", valueOf(10), VALID, "clothes", valueOf(1), 10)
        List<Product> product = [product1]
        Map<Product, BigDecimal> productDiscount = [(product1): 0.9]

        discountRepository.findDiscountRule() >> Optional.of(discountRule)
        productRepository.findAllById(_) >> product
        discountRule.calculateDiscount(_) >> productDiscount

        when:
        1 * mockRange.belongsTo(_) >> true
        1 * mockCondition.isSatisfied(_) >> true
        1 * mockCondition.getDiscount() >> 0.9
        def result = orderApplicationService.previewOrder(orderReqDto)

        then:
        result.totalPrice == valueOf(30)
        result.paidPrice == valueOf(27)
        result.productDetails.get(0).discount == valueOf(0.9)
        result.productDetails.get(0).discountedPrice == valueOf(27)
        result.productDetails.get(0).priceDifference == valueOf(3)
    }

    def "should preview order detail with no discount rule"() {
        given:
        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(1, 3))
        OrderReqDto orderReqDto = new OrderReqDto(UUID.fromString("AC0E8B2C-4721-47FB-A784-92DC226FF84F"), orderProducts)

        def product1 = new Product(1, "testProduct", valueOf(10), VALID, "clothes", valueOf(1), 10)
        List<Product> product = [product1]

        productRepository.findAllById(_) >> product
        discountRepository.findDiscountRule() >> Optional.empty()

        when:
        def result = orderApplicationService.previewOrder(orderReqDto)

        then:
        result.totalPrice == valueOf(30)
        result.paidPrice == valueOf(30)
        result.productDetails.get(0).discount == valueOf(1)
        result.productDetails.get(0).discountedPrice == valueOf(30)
        result.productDetails.get(0).priceDifference == valueOf(0)
    }

    def "should send order creation message when saving order successfully"() {
        given:
        Integer PRODUCT_ID = 11
        Integer QUANTITY = 10
        String CATEGORY = "clothes"

        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(PRODUCT_ID, QUANTITY))
        OrderReqDto orderReqDto = new OrderReqDto(UUID.fromString("AC0E8B2C-4721-47FB-A784-92DC226FF84F"), orderProducts)

        List<Product> products = [new Product(PRODUCT_ID, "testProduct", BigDecimal.TEN, VALID, CATEGORY, valueOf(0.8), 10)]
        productRepository.findAllById(_) >> products

        List<ProductDetail> productDetailList = [new ProductDetail(PRODUCT_ID, "testProduct", BigDecimal.TEN, CATEGORY, 10, valueOf(0.8))]
        Order orderSaved = OrderFactory.buildOrder(UUID.fromString("ac0e8b2c-4721-47fb-a784-92dc226ff84f"), productDetailList)
        orderRepository.save(_) >> orderSaved

        when:
        orderApplicationService.createOrder(orderReqDto)

        then:
        1 * orderDataServiceClient.sendOrderCreationData(orderSaved)
    }

    def "should not send order creation message when saving order falied"() {
        given:
        Integer PRODUCT_ID = 11
        Integer QUANTITY = 10

        List<OrderProductReqDto> orderProducts = List.of(new OrderProductReqDto(PRODUCT_ID, QUANTITY))
        OrderReqDto orderReqDto = new OrderReqDto(UUID.fromString("AC0E8B2C-4721-47FB-A784-92DC226FF84F"), orderProducts)

        List<Product> products = List.of()
        productRepository.findAllById(_) >> products

        when:
        orderApplicationService.createOrder(orderReqDto)

        then:
        thrown(BusinessException)
        0 * orderDataServiceClient.sendOrderCreationData(_)
    }
}