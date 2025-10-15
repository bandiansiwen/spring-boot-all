package com.imp.all.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.imp.all.annotation.CustomMergeStrategy;
import com.imp.all.entity.Member;
import com.imp.all.entity.Order;
import com.imp.all.entity.OrderData;
import com.imp.all.entity.Product;
import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.utils.LocalJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Longlin
 * @date 2022/1/12 22:48
 * @description https://mp.weixin.qq.com/s/1c9ojd9GaUO70IhJu3jO8A
 * 项目地址：https://github.com/alibaba/easyexcel
 * 官方文档：https://www.yuque.com/easyexcel/doc/easyexcel
 */
@RestController
@Api(value = "EasyPoiController", tags = "EasyPoi导入导出测试")
@RequestMapping("/easyexcel")
public class EasyExcelController {

    @SneakyThrows(IOException.class)
    @ApiOperation(value = "导出会员列表Excel")
    @RequestMapping(value = "/exportMemberList", method = RequestMethod.GET)
    public void exportMemberList(HttpServletResponse response) {
        setExcelRespProp(response, "会员列表");
        List<Member> memberList = LocalJsonUtil.getListFromJson("excel/members.json", Member.class);
        EasyExcel.write(response.getOutputStream())
                .head(Member.class)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("会员列表")
                .doWrite(memberList);
    }

    /**
     * 设置excel下载响应头属性
     */
    private void setExcelRespProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }

    @SneakyThrows
    @ApiOperation("从Excel导入会员列表")
    @RequestMapping(value = "/importMemberList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<?> importMemberList(@RequestPart("file") MultipartFile file) {
        List<Member> memberList = EasyExcel.read(file.getInputStream())
                .head(Member.class)
                .sheet()
                .doReadSync();
        return CommonResult.success(memberList);
    }

    @SneakyThrows
    @ApiOperation(value = "导出订单列表Excel")
    @RequestMapping(value = "/exportOrderList", method = RequestMethod.GET)
    public void exportOrderList(HttpServletResponse response) {
        List<Order> orderList = getOrderList();
        List<OrderData> orderDataList = convert(orderList);
        setExcelRespProp(response, "订单列表");
        EasyExcel.write(response.getOutputStream())
                .head(OrderData.class)
                .registerWriteHandler(new CustomMergeStrategy(OrderData.class))
                .excelType(ExcelTypeEnum.XLSX)
                .sheet("订单列表")
                .doWrite(orderDataList);
    }

    private List<Order> getOrderList() {
        List<Order> orderList = LocalJsonUtil.getListFromJson("excel/orders.json", Order.class);
        List<Product> productList = LocalJsonUtil.getListFromJson("excel/products.json", Product.class);
        List<Member> memberList = LocalJsonUtil.getListFromJson("excel/members.json", Member.class);
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            order.setMember(memberList.get(i));
            order.setProductList(productList);
        }
        return orderList;
    }

    private List<OrderData> convert(List<Order> orderList) {
        List<OrderData> result = new ArrayList<>();
        for (Order order : orderList) {
            List<Product> productList = order.getProductList();
            for (Product product : productList) {
                OrderData orderData = new OrderData();
                BeanUtil.copyProperties(product,orderData);
                BeanUtil.copyProperties(order,orderData);
                result.add(orderData);
            }
        }
        return result;
    }
}
