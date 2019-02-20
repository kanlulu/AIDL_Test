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
    //不是必须的 但是如果我们不创建这个属性
    //系统会为我们创建一个serialVersionUID
    //如果我们在反序列化时修改了该对象的任何属性或类型，则反序列化会失败且报错 InvalidClassException。
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
