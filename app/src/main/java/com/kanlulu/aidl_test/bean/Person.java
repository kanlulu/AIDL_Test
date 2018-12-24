package com.kanlulu.aidl_test.bean;

import java.io.Serializable;

/**
 * Created by kanlulu
 * DATE: 2018/11/30 14:45
 */
public class Person implements Serializable {
    private String name;
    private int age;
    private String desc;
    private static final long serialVersionUID = 8829975621220483374L;

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
