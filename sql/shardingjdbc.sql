CREATE TABLE `orders_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_2` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_3` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_4` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_5` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_6` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_7` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_8` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `orders_9` (
  `id` bigint NOT NULL COMMENT '主键',
  `order_no` varchar(255) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `pay_amount` decimal(20,2) NOT NULL DEFAULT '0' COMMENT '支付金额',
  `order_status` int NOT NULL COMMENT '订单状态 1-已预约 2-已取车 3-已还车 4-已取消',
  `pay_status` int NOT NULL DEFAULT '0' COMMENT '支付状态 0-未支付 1-支付成功',
  `pay_method` int DEFAULT NULL COMMENT '支付方式 1-微信 2-支付宝',
  `pay_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '支付时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_0` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_1` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_2` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_3` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_4` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_5` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_6` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_7` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_8` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students_9` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '名字',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

