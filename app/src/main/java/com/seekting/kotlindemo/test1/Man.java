package com.seekting.kotlindemo.test1;

/**
 * Created by Administrator on 2017/9/26.
 */

public class Man {
    private String name;
    private int age;

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public Man(int a, String b){

    }

    public void say() {
        System.out.println("my name is " + name + " my age is" + age);
    }
    public void t(int a,String b){
    }
}
