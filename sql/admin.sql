# 开发中往往都会有这四个字段:
# version（乐观锁）
# deleted（逻辑删除）
# create_by（创建人）
# create_time（创建时间）
# modified_by（修改人）
# modify_time（修改时间）

DROP TABLE IF EXISTS tb_sys_user;
CREATE TABLE `tb_sys_user`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `username`    varchar(64)         DEFAULT NULL,
    `password`    varchar(64)         DEFAULT NULL,
    `icon`        varchar(500)        DEFAULT NULL COMMENT '头像',
    `phone`       varchar(50)         DEFAULT NULL COMMENT '手机号',
    `email`       varchar(100)        DEFAULT NULL COMMENT '邮箱',
    `nick_name`   varchar(200)        DEFAULT NULL COMMENT '昵称',
    `note`        varchar(500)        DEFAULT NULL COMMENT '备注信息',
    `login_time`  datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后登录时间',
    `status`      int(1)              DEFAULT '1' COMMENT '帐号启用状态：0->禁用；1->启用',
    `create_by`   varchar(50)         DEFAULT NULL COMMENT '创建人',
    `create_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modified_by` varchar(50)         DEFAULT NULL COMMENT '修改人',
    `modify_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `version`     int                 DEFAULT NULL COMMENT '版本',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户表';

DROP TABLE IF EXISTS student;
CREATE TABLE `student` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '学号',
    `name` VARCHAR(200) NULL DEFAULT NULL COMMENT '姓名',
    `age` INT NULL DEFAULT NULL COMMENT '年龄',
    `phone` VARCHAR(255) NULL DEFAULT NULL COMMENT '手机号',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8 COMMENT='学生信息';

DROP TABLE IF EXISTS student;
CREATE TABLE `student`
(
    `id`    bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '学号',
    `name`  VARCHAR(200) NULL DEFAULT NULL COMMENT '姓名',
    `age`   INT(10)      NULL DEFAULT NULL COMMENT '年龄',
    `phone` VARCHAR(255) NULL DEFAULT NULL COMMENT '手机号',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
    COMMENT ='学生信息';

