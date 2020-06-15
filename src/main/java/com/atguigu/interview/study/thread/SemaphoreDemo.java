package com.atguigu.interview.study.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/*
    争车位
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        // 模拟3个停车位
        Semaphore semaphore = new Semaphore(3);
        // 模拟6部车
        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire(); // 抢占
                    System.out.println(Thread.currentThread().getName() + "\t抢到车位");
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println(Thread.currentThread().getName() + "\t停车3秒钟，离开停车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }, String.valueOf(i)).start();
        }
    }
}
