package com.atguigu.interview.study.thread;

/*
    公平锁：是指多个线程按照申请锁的顺序来获取锁类似排队打饭 先来后到

    非公平锁：是指在多线程获取锁的顺序并不是按照申请锁的顺序,有可能后申请的线程比先申请的
    线程优先获取到锁,在高并发的情况下,有可能造成优先级反转或者饥饿现象

    公平锁/非公平锁
    并发包ReentrantLock的创建可以指定构造函数的boolean类型来得到公平锁或者非公平锁
    默认是非公平锁

    关于两者区别

    公平锁就是很公平，在并发环境中，每个线程在获取锁时会先查看此锁维护的等待
    队列，如果为空，或者当前线程是等待队列的第一个，就占有锁，否则就会加入到等待队列中，以
    后会按照FIFO的规则从队列中取到自己。

    非公平锁比较粗鲁，上来就直接尝试占有资源，如果尝试失败，就再采用类似公平锁的方式

    Java ReentrantLock而言,
    通过构造函数指定该锁是否是公平锁 默认是非公平锁 非公平锁的优点在于吞吐量必公平锁大.

    对于synchronized而言 也是一种非公平锁.

    ReentrantLock/synchronized就是一个典型的可重入锁

    可重入锁最大的作用就是避免死锁

    public ReentrantLock() {
        // 非公平锁，默认为非公平锁 ReentrantLock(false)
        sync = new NonfairSync();
    }

    public ReentrantLock(boolean fair) {
        // 公平锁
        sync = fair ? new FairSync() : new NonfairSync();
    }

    可重入锁，也叫递归锁
    指的是同一个线程外层函数获得锁之后，内层递归函数仍然能获取该锁的代码，在同一个线程在外层
    方法获取锁的时候，在进入内层方法会自动获取锁

    也就是说，线程可以进入任何一个它已经拥有的锁所同步着的代码块。

    案例1：synchronized是一个典型的可重入锁

    t1	invoked Phone.sendSMS()   t1线程在外层方法获取锁的时候
    t1	invoked Phone.sendEmail() t1在进入内层方法会自动获取锁

    t2	invoked Phone.sendSMS()
    t2	invoked Phone.sendEmail()

    案例2：ReenterLock是一个典型的可重入锁
 */

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Phone implements Runnable { // 资源类
    // synchronized可重入锁
    public synchronized void sendSMS() {
        System.out.println(Thread.currentThread().getName() + "\t invoked Phone.sendSMS()");
        sendEmail();
    }

    public synchronized void sendEmail() {
        System.out.println(Thread.currentThread().getName() + "\t invoked Phone.sendEmail()");
    }

    // --------------------------------------------------------------------------

    Lock lock = new ReentrantLock();

    @Override
    public void run() {
        get();
    }

    public void get() {
        lock.lock();
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t invoked get()");
            set();
        } finally {
            lock.unlock();
            lock.unlock();
        }
    }

    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t invoked set()");
        } finally {
            lock.unlock();
        }
    }
}

public class ReenterLockDemo {
    public static void main(String[] args) {
        Phone phone = new Phone();

        new Thread(() -> {
            phone.sendSMS();
        }, "t1").start();

        new Thread(() -> {
            phone.sendSMS();
        }, "t2").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("---------------------");

        Thread t3 = new Thread(phone, "t3");
        Thread t4 = new Thread(phone, "t4");

        t3.start();
        t4.start();
    }
}
