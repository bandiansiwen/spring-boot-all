package com.imp.all.demos.proxy;

/**
 * @author Longlin
 * @date 2021/9/7 15:45
 * @description
 */
public class SubjectImpl implements Subject{
    @Override
    public String sayHello() {
        System.out.println(" Hello World");
        return "success";
    }
}
