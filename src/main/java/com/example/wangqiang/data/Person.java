package com.example.wangqiang.data;

import org.litepal.crud.DataSupport;

/**
 * Created by wangqiang on 2017/4/1.
 */

public class Person extends DataSupport {
    private String name;
    private int age;

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
}
