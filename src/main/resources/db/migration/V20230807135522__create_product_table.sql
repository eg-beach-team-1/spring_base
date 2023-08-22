CREATE TABLE IF NOT EXISTS `product`
(
    `id`                   INT            NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                 VARCHAR(32)    NOT NULL                COMMENT '商品名称',
    `price`                DECIMAL(10,2)                          COMMENT '价格',
    `status`               VARCHAR(32)    NOT NULL                COMMENT '状态',
    `discount`             DECIMAL(10,2)  NOT NULL                COMMENT '折扣',
    `stock`                INT            NOT NULL                COMMENT '库存',
PRIMARY KEY(`id`)
USING BTREE
)ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
