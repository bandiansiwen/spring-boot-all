package com.imp.all.demos.designMode.createMode.prototype;

/**
 * @author Longlin
 * @date 2022/2/10 0:08
 * @description 具体原型类
 */
public class Realizetype implements Cloneable {

    Realizetype() {
        System.out.println("具体原型创建成功！");
    }

    @Override
    public Realizetype clone() throws CloneNotSupportedException {
        System.out.println("具体原型复制成功！");
        return (Realizetype) super.clone();
    }
}
