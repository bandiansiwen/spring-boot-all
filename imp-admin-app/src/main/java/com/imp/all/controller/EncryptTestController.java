package com.imp.all.controller;

import com.imp.all.advice.ImpDecode;
import com.imp.all.advice.ImpEncode;
import com.imp.all.entity.Student;
import com.imp.all.framework.common.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Longlin
 * @date 2022/10/18 11:10
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/encrypt")
public class EncryptTestController {

    // 传参：Gg/hLfWllxXF7KkzeNTPELCZ3jjxgHL24tJWPhO+O7KS5vAS1ag9xmtjP94L8p8BY+HMggCL1mvVEHEL8+FwSSjWYLln8SZk4CuWil2x4sI=
    @ImpDecode
    @PostMapping("/decode")
    public Object decode (@RequestBody(required = false) Student requestBody) {
        log.info("requestBody={}", requestBody);
        return requestBody;
    }

    @ImpEncode
    @PostMapping("/encode")
    public Object encode (@RequestBody Student requestBody) {
        log.info("requestBody={}", requestBody);
        return CommonResult.success(requestBody);
    }
}
