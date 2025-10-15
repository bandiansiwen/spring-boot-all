package com.imp.all.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Longlin
 * @date 2022/1/12 22:54
 * @description
 */
@Data
public class Order {

    private Long id;
    private String orderSn;
    private Date createTime;
    private String receiverAddress;

    private Member member;
    private List<Product> productList;
}