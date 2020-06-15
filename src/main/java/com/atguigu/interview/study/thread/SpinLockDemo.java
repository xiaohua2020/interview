package com.atguigu.interview.study.thread;

/*
    自旋锁：是指尝试获取锁的线程不会立即阻塞，而是采用循环的方式去尝试获取锁，
    这样的好处是减少线程上下文切换的消耗，缺点是循环会消耗CPU

    理论 代码 小总结
 */


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName() + "\t invoked myLock()");

        // 自旋核心
        // 期待值、更新值
        // false 终止循环
        while (!atomicReference.compareAndSet(null, thread)) {
            // 自旋锁：是指尝试获取锁的线程不会立即阻塞，而是采用循环的方式去尝试获取锁，
            // 这样的好处是减少线程上下文切换的消耗，缺点是循环会消耗CPU
        }
    }

    public void myUnlock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(Thread.currentThread().getName() + "\t invoked myUnlock()");
    }


    public static void main(String[] args) {

        SpinLockDemo spinLockDemo = new SpinLockDemo();

        new Thread(() -> {
            spinLockDemo.myLock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockDemo.myUnlock();
        }, "AA").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            spinLockDemo.myLock();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            spinLockDemo.myUnlock();
        }, "BB").start();
    }
}
