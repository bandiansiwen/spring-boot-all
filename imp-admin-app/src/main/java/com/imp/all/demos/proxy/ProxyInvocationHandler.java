package com.imp.all.demos.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Longlin
 * @date 2021/9/7 15:30
 * @description
 */
public class ProxyInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(" 🧱 🧱 🧱 进入代理调用处理器 ");
        return "success";
    }
}
