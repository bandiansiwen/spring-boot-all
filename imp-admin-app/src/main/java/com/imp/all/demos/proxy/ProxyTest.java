package com.imp.all.demos.proxy;

import java.lang.reflect.Proxy;

/**
 * @author Longlin
 * @date 2021/9/7 15:31
 * @description
 */
public class ProxyTest {

    public static void main(String[] args) {

        Subject subject = new SubjectImpl();
        Subject proxy = (Subject) Proxy
                .newProxyInstance(
                        subject.getClass().getClassLoader(),
                        subject.getClass().getInterfaces(),
                        new ProxyInvocationHandler2(subject));
        proxy.sayHello();

        Subject o = (Subject)Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), new Class[]{Subject.class}, new ProxyInvocationHandler());
        o.sayHello();
    }
}
