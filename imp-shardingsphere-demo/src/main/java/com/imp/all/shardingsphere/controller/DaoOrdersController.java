package com.imp.all.shardingsphere.controller;

import com.imp.all.shardingsphere.annotation.DBMaster;
import com.imp.all.shardingsphere.dao.DaoOrdersDao;
import com.imp.all.shardingsphere.entity.DaoOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Longlin
 * @date 2023/2/9 15:34
 * @description
 */
@RestController
@RequestMapping("/daoOrder")
public class DaoOrdersController {

    @Autowired
    private DaoOrdersDao ordersDao;

    @GetMapping("/saveOrders")
    @DBMaster // 设置主库路由标记，强制读主库
    public DaoOrders saveOrders(@RequestParam String orderNo, @RequestParam(required = false) Long userId) {
        DaoOrders orders = new DaoOrders();
        Long id = Long.valueOf(orderNo);
        orders.setId(id);
        orders.setOrderNo(orderNo);
        orders.setUserId(userId!=null?userId: Long.valueOf(1L));
        orders.setPayAmount(new BigDecimal("32"));
        orders.setOrderStatus(1);
        orders.setPayStatus(1);
        orders.setPayMethod(1);
        orders.setPayTime(new Date());
        orders.setCreatedAt(new Date());
        orders.setUpdatedAt(new Date());
        ordersDao.insert(orders);

        // 设置主库路由标记，强制读主库
//        HintManager hintManager = HintManager.getInstance();
//        hintManager.setWriteRouteOnly();

        ordersDao.getOrderByNo(orderNo);
        ordersDao.getOrderByUserIdAndId(id, userId);
        DaoOrders order = ordersDao.getOrderByUserIdAndId(id, userId);

//        hintManager.close();
        return order;
    }

    @GetMapping("/getOrders")
    public DaoOrders getOrders(@RequestParam String orderNo) {
        return ordersDao.getOrderByNo(orderNo);
    }

    // userId 为分库字段，则只查一个库，查库中所有的表
    @GetMapping("/getOrdersByUserId")
    public DaoOrders getOrdersById(@RequestParam Long userId) {
        return ordersDao.getOrderByUserId(userId);
    }

    // id 为分表字段，则查两个库，每个库中一个表
    @GetMapping("/getOrdersById")
    public DaoOrders getOrdersId(@RequestParam Long id) {
        return ordersDao.getOrderById(id);
    }

    // id 为分表字段，userId 为分库字段，则查一个库，一个表
    @GetMapping("/getOrdersByIdAndUserId")
    public DaoOrders getOrdersByIdAndUserId(@RequestParam Long id, @RequestParam Long userId) {
        return ordersDao.getOrderByUserIdAndId(id, userId);
    }

    // 多库多表查询
    @GetMapping("/getOrdersList")
    public List<DaoOrders> getOrdersList() {
        return ordersDao.getOrderList();
    }
}
