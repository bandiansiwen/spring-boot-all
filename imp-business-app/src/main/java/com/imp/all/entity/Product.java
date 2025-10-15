package com.imp.all.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Longlin
 * @date 2021/12/6 15:47
 * @description
 */

@Data
public class Product {

    private Long id;
    private String productSn;
    private String name;
    private String subTitle;
    private String brandName;
    private BigDecimal price;
    private Integer count;
}