package com.imp.all.controller;

import com.imp.all.framework.web.apiVersion.annotation.ApiVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Longlin
 * @date 2021/4/22 18:59
 * @description
 */
@RestController
@RequestMapping("/api/version")
@Api(value = "ApiVersionController", tags = "接口多版本")
public class ApiVersionController {

    @GetMapping("/hello")
    @ApiOperation(value = "测试接口")
    public String hello0(){
        return "hello 0";
    }

    @GetMapping("/hello")
    @ApiVersion(value = "1.0.1")
    public String hello1(){
        return "hello 1.0.1";
    }

    @GetMapping("/hello")
    @ApiVersion(value = "1.0.2")
    public String hello2(){
        return "hello 1.0.2";
    }
}
