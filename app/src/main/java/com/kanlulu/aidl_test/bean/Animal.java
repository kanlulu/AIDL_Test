package com.kanlulu.aidl_test.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kanlulu
 * DATE: 2018/11/30 15:54
 *
 * 如果我们只需要将对象在内存中传递，那么首选Parcelable来对对象进行序列化与反序列化操作
 */
public class Animal implements Parcelable {
    private String name;
    private int age;
    private String desc;

    public Animal() {
    }

    /**
     * 需要自己创建包含全部属性的构造方法
     */
    public Animal(String name, int age, String desc) {
        this.name = name;
        this.age = age;
        this.desc = desc;
    }

    /**
     * 自动生成的
     * 不要改动顺序，序列化与反序列化受顺序的影响
     */
    protected Animal(Parcel in) {
        name = in.readString();
        age = in.readInt();
        desc = in.readString();
    }

    /**
     * 自动生成的
     * 反序列化
     */
    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel in) {
            return new Animal(in);
        }

        @Override
        public Animal[] newArray(int size) {
            return new Animal[size];
        }
    };

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

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", desc='" + desc + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        //几乎都返回 0，除非当前对象中存在文件描述符时为 1
        return 0;
    }

    /**
     * 序列化
     * 不能改动顺序
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(desc);
    }
}
