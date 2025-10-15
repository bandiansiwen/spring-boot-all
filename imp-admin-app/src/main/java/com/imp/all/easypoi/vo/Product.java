package com.imp.all.easypoi.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Longlin
 * @date 2021/12/6 15:47
 * @description
 */

@Data
public class Product {

    @Excel(name = "ProductID", width = 10)
    private Long id;
    @Excel(name = "商品SN", width = 20)
    private String productSn;
    @Excel(name = "商品名称", width = 20)
    private String name;
    @Excel(name = "商品副标题", width = 30)
    private String subTitle;
    @Excel(name = "品牌名称", width = 20)
    private String brandName;
    // type 导出类型	默认 1  1是文本, 2是图片, 3是函数, 10是数字 默认是文本
    @Excel(name = "商品价格", width = 10, type = 10)
    private BigDecimal price;
    @Excel(name = "购买数量", width = 10, suffix = "件", type = 10)
    private Integer count;
}