package com.imp.all.shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Longlin
 * @date 2023/2/9 15:28
 * @description
 */
@Data
@TableName("students")
public class Students implements Serializable {

    @TableId
    private Long id;

    /**
     * 订单号
     */
    private String name;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改时间
     */
    private Date updatedAt;
}
