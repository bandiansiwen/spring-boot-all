package com.imp.all.demos.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Longlin
 * @date 2021/9/7 15:45
 * @description
 */
public class ProxyInvocationHandler2 implements InvocationHandler {

    private Object target;

    public ProxyInvocationHandler2(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(" 🧱 🧱 🧱 进入代理调用处理器 ");
        return method.invoke(target, args);
    }
}
