package com.imp.all.shardingsphere.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imp.all.shardingsphere.entity.Students;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Longlin
 * @date 2023/2/9 15:31
 * @description
 */
@Mapper
public interface StudentDao extends BaseMapper<Students> {

    Students getStudentByUserId(Long userId);

    Students getStudentById(Long id);

    Students getStudentByUserIdAndId(@Param("id") Long id, @Param("userId") Long userId);

    List<Students> getList();
}
