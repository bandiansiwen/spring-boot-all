package com.imp.all.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Longlin
 * @date 2021/12/28 15:11
 * @description
 */

@Data
public class Student {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer age;
    private String phone;
}
