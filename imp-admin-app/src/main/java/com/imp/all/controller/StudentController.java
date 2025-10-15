package com.imp.all.controller;

import cn.hutool.core.util.StrUtil;
import com.imp.all.dao.StudentMapper;
import com.imp.all.entity.Student;
import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.multiDatasource.DBTypeEnum;
import com.imp.all.multiDatasource.DataSourceContextHolder;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Longlin
 * @date 2021/12/28 15:13
 * @description
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentMapper studentMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @GetMapping("/insert")
    public String insertStudent(@RequestParam("phone") String phone, @RequestParam("name") String name, @RequestParam("age") Integer age) {
        Student student = new Student();
        student.setName(name);
        student.setPhone(phone);
        student.setAge(age);
        int result = studentMapper.insert(student);
        return "添加结果: " + result;
    }

    @GetMapping("/query")
    public CommonResult queryStudent(@RequestParam("name") String name, @RequestParam("age") Integer age) {
        Student student = studentMapper.findByName(name);
        return CommonResult.success(student);
    }

    /**
     * TODO
     * insert 走 master， query 走 slave（需要改一下）
     */
    @GetMapping("/insertAndQuery")
    public CommonResult insertAndQueryStudent(@RequestParam("phone") String phone, @RequestParam("name") String name, @RequestParam("age") Integer age) {
        Student student = new Student();
        student.setName(name);
        student.setPhone(phone);
        student.setAge(age);
        int result = studentMapper.insert(student);
        Student stu = studentMapper.findByName(name);
        return CommonResult.success(stu);
    }

    @GetMapping("/add")
    public String addStudent(@RequestParam("phone") String phone, @RequestParam("name") String name, @RequestParam("age") Integer age) {
        int result = studentMapper.addStudent(phone, name, age);
        return "添加结果: " + result;
    }

    @PostMapping("/batchAdd")
    public String batchAddStudent(@RequestBody List<Student> students) {
        int result = studentMapper.batchAddStudent(students, "123456");
        return "添加结果: " + result;
    }

    /**
     * 1. 修改数据库连接参数加上allowMultiQueries=true
     * 2. 多条sql语句用分号隔开
     */
    @PostMapping("/batchDelete")
    public String batchDelete(@RequestBody List<Integer> students) {
        int result = studentMapper.batchDeleteStudent(students);
        return "添加结果: " + result;
    }

    @GetMapping("/findByPhone")
    public Student findStudent(@RequestParam("phone") String phone) {
        return studentMapper.findStudentByPhone(phone);
    }

    @GetMapping("/findByName")
    public CommonResult<?> findByName(@RequestParam("name") String name, @RequestParam(value = "dbType", required = false) String dbType) {
        if (StrUtil.isNotEmpty(dbType)) {
            try {
                DBTypeEnum dbTypeEnum = DBTypeEnum.valueOf(dbType);
                DataSourceContextHolder.customSet(dbTypeEnum);
            }catch (IllegalArgumentException e) {
                return CommonResult.failed("dbType 不存在");
            }
        }
        Student student = studentMapper.findByName(name);
        return CommonResult.success(student);
    }

    // foreach 效率最高
    @PostMapping("/foreachInsert")
    public String foreachInsert(Integer num) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            Student stu = new Student();
            stu.setAge(i);
            stu.setName("张三"+i);
            students.add(stu);
        }
        long startTime = System.nanoTime();
        int result = studentMapper.batchAddStudent(students, "123456");
        long expendTime = System.nanoTime()-startTime;
        return "添加结果: " + result + "; 消耗时长: " + expendTime/1000/1000;
    }

    // 耗时最长
    @PostMapping("/batchInsert")
    public String batchInsert(Integer num) {

        long startTime = System.nanoTime();

        try (SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            for (int i = 0; i < num; i++) {
                studentMapper.insertStudent("张三" + i, i, "123456");
            }
            session.commit();
        }

        long expendTime = System.nanoTime()-startTime;
        return "消耗时长: " + expendTime/1000/1000;
    }

    // 耗时次长
    @PostMapping("/batchJdbcInsert")
    public String batchJdbcInsert(Integer num) throws SQLException {

        long startTime = System.nanoTime();

        Connection connection = sqlSessionFactory.openSession().getConnection();
        if (connection != null) {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(
                    "insert into student(name, age, phone) values(?, ?, ?)");
            for (int i = 0; i < num; i++) {
                ps.setString(1, "王五"+i);
                ps.setInt(2, i);
                ps.setString(3,"123456");
                ps.addBatch();
            }
            ps.executeBatch();
            connection.commit();
            connection.close();
        }

        long expendTime = System.nanoTime()-startTime;
        return "消耗时长: " + expendTime/1000/1000;
    }
}
