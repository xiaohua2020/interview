package com.atguigu.interview.study.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
    多个线程同时读一个资源类没有任何问题，所以为了满足并发量，读取共享资源应该可以同时进行
    但是如果有一个线程想去写共享资源，就不应该再有其它线程可以对该资源进行读或写

    总结：

    读-读能共存
    读-写不能共存
    写-写不能共存

    写操作：原子 + 独占，不可分断（洗澡）

    Java三大并发包
    java.util.concurrent
    java.util.concurrent.atomic
    java.util.concurrent.locks


    缓存框架的三个重要方法：读、写、清空
 */
class MyCache { // 缓存资源类

    private volatile Map<String, Object> cacheMap = new HashMap<>();

    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    // 写方法
    public void put(String key, Object value) {

        rwLock.writeLock().lock();

        try {
            System.out.println(Thread.currentThread().getName() + "\t正在写入：" + key);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cacheMap.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t写入完成.");
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    // 读方法
    public void get(String key) {

        rwLock.readLock().lock();

        try {
            System.out.println(Thread.currentThread().getName() + "\t正在读取...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Object value = cacheMap.get(key);
            System.out.println(Thread.currentThread().getName() + "\t读取完成：" + value);
        } finally {
            rwLock.readLock().unlock();
        }
    }
}

public class ReadWriteLockDemo {

    public static void main(String[] args) {

        MyCache myCache = new MyCache();

        // 写线程
        for (int i = 1; i <= 10; i++) {
            final int intTemp = i;
            new Thread(() -> {
                myCache.put(intTemp + "", intTemp + "");
            }, String.valueOf(i)).start();
        }

        // 读线程
        for (int i = 1; i <= 10; i++) {
            final int intTemp = i;
            new Thread(() -> {
                myCache.get(intTemp + "");
            }, String.valueOf(i)).start();
        }
    }
}