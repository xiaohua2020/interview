package com.atguigu.interview.study.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    公平锁
    是指多个线程按照申请锁的顺序来获取锁类似排队打饭 先来后到

    非公平锁
    是指在多线程获取锁的顺序并不是按照申请锁的顺序,有可能后申请的线程比先申请的线程优先获取到锁,
    在高并发的情况下,有可能造成优先级反转或者饥饿现象
 */
public class T1 {

    volatile int number = 0;

    public void add() {
        number++;
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock(); // 默认非公平锁
    }
}
