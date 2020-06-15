package com.atguigu.interview.study.thread;

/*
    我爸是李刚，有事找我爸
 */
public class MyObject {

    public static void main(String[] args) {

        Object object = new Object();
        System.out.println(object.getClass().getClassLoader().getParent().getParent());
        System.out.println(object.getClass().getClassLoader().getParent());
        System.out.println(object.getClass().getClassLoader());
        System.out.println("---------");
        MyObject object2 = new MyObject();
        System.out.println(object2.getClass().getClassLoader().getParent().getParent());
        System.out.println(object2.getClass().getClassLoader().getParent());
        System.out.println(object2.getClass().getClassLoader());

        Thread thread = new Thread();
        thread.start();
    }
}
