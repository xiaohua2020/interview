package com.atguigu.interview.study.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/*
    ABA问题的解决    AtomicStampedReference
 */
public class ABADemo {

    static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        System.out.println("-------------以下是ABA问题的产生------------");

        // 线程1
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();

        // 线程2
        new Thread(() -> {
            try {
                // 暂停一秒钟，保证上面的T1线程完成了一次ABA操作
                TimeUnit.SECONDS.sleep(1);
                System.out.println(atomicReference.compareAndSet(100, 2019) + "\t" + atomicReference.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2").start();

        // 休息2秒，保证上面的线程完成ABA操作
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-------------以下是ABA问题的解决------------");

        // 线程3
        new Thread(() -> {
            // 时间戳
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t第1次版本号：" + stamp);
            // 休息1秒，保证当前的线程完成ABA操作（保证t4与t3拿到的是同一个版本号）
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 期望值、更新值、期望版本号、更新版本号
            // 条件成立，修改为101
            // 100 ---> 101 ---> 100
            atomicStampedReference.compareAndSet(100, 101, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t第2次版本号：" + atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t第3次版本号：" + atomicStampedReference.getStamp());
        }, "t3").start();

        // 线程4
        new Thread(() -> {
            // 时间戳
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t第1次版本号：" + stamp);
            // 休息3秒，保证上面的t3线程完成一次ABA操作（多休息2秒钟保证t3足够完成ABA操作）
            // 100 ---> 2019
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean result = atomicStampedReference.compareAndSet(100, 2019, stamp, stamp + 1); // 版本号2，实际真实版本号为3
            System.out.println(Thread.currentThread().getName() + "\t修改成功否：" + result + "\t，当前最新实际版本号：" + atomicStampedReference.getStamp());
            System.out.println(Thread.currentThread().getName() + "\t当前实际最新值：" + atomicStampedReference.getReference());
        }, "t4").start();
    }
}
