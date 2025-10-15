package com.imp.all.shardingsphere.controller;

import com.imp.all.shardingsphere.dao.StudentDao;
import com.imp.all.shardingsphere.entity.Students;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author Longlin
 * @date 2023/2/9 15:34
 * @description
 */
@RestController
@RequestMapping("/students")
public class StudentsController {

    @Autowired
    private StudentDao studentDao;

    @GetMapping("/save")
    public void saveOrders(@RequestParam(required = false) Long userId) {
        Students student = new Students();
        student.setUserId(userId!=null?userId: Long.valueOf(1L));
        student.setName("学生" + userId);
        student.setCreatedAt(new Date());
        student.setUpdatedAt(new Date());
        studentDao.insert(student);
    }

    // userId 为分库字段，则只查一个库，查库中所有的表
    @GetMapping("/getByUserId")
    public Students getByUserId(@RequestParam Long userId) {
        return studentDao.getStudentByUserId(userId);
    }

    // id 为分表字段，则查两个库，每个库中一个表
    @GetMapping("/getById")
    public Students getById(@RequestParam Long id) {
        return studentDao.getStudentById(id);
    }

    // id 为分表字段，userId 为分库字段，则查一个库，一个表
    @GetMapping("/getByIdAndUserId")
    public Students getByIdAndUserId(@RequestParam Long id, @RequestParam Long userId) {
        return studentDao.getStudentByUserIdAndId(id, userId);
    }

    // 多库多表查询
    @GetMapping("/getList")
    public List<Students> getList() {
        return studentDao.getList();
    }
}
