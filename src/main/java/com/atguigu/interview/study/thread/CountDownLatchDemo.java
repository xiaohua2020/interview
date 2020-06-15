package com.atguigu.interview.study.thread;

import java.util.concurrent.CountDownLatch;

/*
    秦灭六国，一统华夏
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t国，被灭.");
                countDownLatch.countDown();
            }, CountryEnum.forEachCountryEnum(i).getRetMessage()).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t秦帝国，一统华夏.");
        System.out.println();

        System.out.println(CountryEnum.ONE);
        System.out.println(CountryEnum.ONE.getRetCode());
        System.out.println(CountryEnum.ONE.getRetMessage());
    }

    private static void closeDoor() throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t上完自习，离开教室.");
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t班长最后关门走人.");
    }
}
