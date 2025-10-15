package com.imp.all.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imp.all.entity.Student;
import com.imp.all.multiDatasource.DBTypeEnum;
import com.imp.all.multiDatasource.DSRoute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Longlin
 * @date 2021/12/28 15:14
 * @description
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    int addStudent(@Param("phone") String phone, @Param("name") String name, @Param("age") Integer age);

    int batchAddStudent(List<Student> students, String phoneNumber);

    int batchDeleteStudent(@Param("students") List<Integer> students);

    Student findStudentByPhone(@Param("phone") String phone);

    @DSRoute(DBTypeEnum.SLAVE1)
    Student findByName(@Param("name") String name);

    int insertStudent(String name, Integer age, String phoneNumber);
}
