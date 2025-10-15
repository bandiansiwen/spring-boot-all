package com.imp.all;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Longlin
 * @date 2021/8/11 10:37
 * @description
 */
@Setter
@Getter
public class TestObj implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer age;

    transient String id;

    final String ss = "aaa";

    public static String call = "我擦";

    private TestObj.User user;

    public void initUser() {
       this.user = new User();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Setter
    @Getter
    public static class User implements Serializable {
        private static final long serialVersionUID = 1L;

        private String userName = "a疯狂拉大神密令范德萨";
    }
}
