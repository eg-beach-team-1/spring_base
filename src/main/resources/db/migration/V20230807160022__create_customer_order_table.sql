CREATE TABLE IF NOT EXISTS `customer_order`
(
    `id`              VARCHAR(64)    NOT NULL COMMENT '主键',
    `customer_id`     VARCHAR(64)    NOT NULL COMMENT '客户ID',
    `total_price`     DECIMAL(10, 2) NOT NULL COMMENT '总价',
    `status`          VARCHAR(32)    NOT NULL COMMENT '订单状态',
    `create_time`     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `product_details` JSON           NOT NULL COMMENT '商品明细',
    PRIMARY KEY (`id`)
        USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
