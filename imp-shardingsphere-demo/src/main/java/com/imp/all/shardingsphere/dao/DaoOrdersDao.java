package com.imp.all.shardingsphere.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imp.all.shardingsphere.entity.DaoOrders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Longlin
 * @date 2023/2/9 15:31
 * @description
 */
@Mapper
public interface DaoOrdersDao extends BaseMapper<DaoOrders> {

    DaoOrders getOrderByNo(String orderNo);

    DaoOrders getOrderByUserId(Long userId);

    DaoOrders getOrderById(Long id);

    DaoOrders getOrderByUserIdAndId(@Param("id") Long id, @Param("userId") Long userId);

    List<DaoOrders> getOrderList();
}
