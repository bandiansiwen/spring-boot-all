package com.imp.all.framework.file.core.client;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Longlin
 * @date 2022/11/21 17:36
 * @description
 */
// @JsonTypeInfo 注解的作用，Jackson 多态
// 1. 序列化到时数据库时，增加 @class 属性。
// 2. 反序列化到内存对象时，通过 @class 属性，可以创建出正确的类型
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface ImpFileClientConfig {
}
