package com.atguigu.interview.study.thread;

public class SingletonDemo {

    private static volatile SingletonDemo instance = null;

    private SingletonDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 我是构造方法SingletonDemo()");
    }

    // 方法加synchronized关键字可以解决（重量级）
    // DCL（Double Check Lock）双端检锁机制（加锁前与加锁后进行判断），双端检锁机制不一定线程安全，
    // 原因是有指令重排序的存在，加入volatile可以禁止指令重排，原因在于某一个线程在执行到第一次检测，
    // 读取到的instance不为null时,instance的引用对象可能没有完成初始化.

    // instance=new SingletonDem(); 可以分为以下步骤(伪代码)
    // memory=allocate();//1.分配对象内存空间
    // instance(memory);//2.初始化对象
    // instance=memory;//3.设置instance的指向刚分配的内存地址,此时instance!=null
    public static SingletonDemo getInstance() {
        if (instance == null) {
            synchronized (SingletonDemo.class) {
                if (instance == null) {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        // 单线程版本
        // System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        // System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        // System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());

        // 多线程版本
        for (int i = 1; i <= 100; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            }, String.valueOf(i)).start();
        }
    }
}
