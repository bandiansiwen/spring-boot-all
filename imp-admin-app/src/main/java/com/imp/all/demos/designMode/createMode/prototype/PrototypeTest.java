package com.imp.all.demos.designMode.createMode.prototype;

/**
 * @author Longlin
 * @date 2022/2/10 0:10
 * @description
 * 原型模式也称为克隆模式，是拷贝出一个新对象
 */
public class PrototypeTest {

    public static void main(String[] args) throws CloneNotSupportedException {
        Realizetype obj1 = new Realizetype();
        Realizetype obj2 = obj1.clone();
        System.out.println("obj1==obj2?" + (obj1 == obj2));
    }
}
