package com.imp.all.shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Longlin
 * @date 2023/2/9 15:28
 * @description
 */
@Data
@TableName("orders")
public class DaoOrders implements Serializable {

    @TableId
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单状态 1-已预约 2-已取车 3-已还车 4-已取消
     */
    private Integer orderStatus;

    /**
     * 支付状态 0-未支付 1-支付成功
     */
    private Integer payStatus;

    /**
     * 支付方式 1-微信 2-支付宝
     */
    private Integer payMethod;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改时间
     */
    private Date updatedAt;
}
