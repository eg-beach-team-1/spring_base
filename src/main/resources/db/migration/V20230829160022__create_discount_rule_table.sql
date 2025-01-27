CREATE TABLE IF NOT EXISTS `discount_rule`
(
    `rule_id`             VARCHAR(64) NOT NULL COMMENT '主键',
    `discount_range`      JSON        NOT NULL COMMENT '优惠范围',
    `discount_conditions` JSON        NOT NULL COMMENT '优惠规则',
    `create_time`         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`rule_id`)
        USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
