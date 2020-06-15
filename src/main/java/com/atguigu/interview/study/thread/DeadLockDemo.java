package com.atguigu.interview.study.thread;

import java.util.concurrent.TimeUnit;

/*
    死锁是指两个或两个以上的进程在执行过程中，因争夺资源而造成的一种互相等待的现象，
    若无外力干涉，那它们都将无法推进下去

    如果系统资源充足，进程的资源请求都能够得到满足，死锁出现的可能性就很低，否则就
    会因争夺有限的资源而陷入死锁

    定位、解决死锁
    1. jps命令定位进程编号
    2. jstack找到死锁查看

    D:\Projects\atguigu\interview>jps -l
    20560 com.atguigu.interview.study.thread.DeadLockDemo
    14580 sun.tools.jps.Jps
    15384
    25816 org.jetbrains.jps.cmdline.Launcher

    D:\Projects\atguigu\interview>jstack 20560
    2020-06-15 09:51:29
    Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.251-b08 mixed mode):

    Java stack information for the threads listed above:
    ===================================================
    "t2":
            at com.atguigu.interview.study.thread.HoldLockThread.run(DeadLockDemo.java:25)
            - waiting to lock <0x000000076b820c70> (a java.lang.String)
            - locked <0x000000076b820ca8> (a java.lang.String)
            at java.lang.Thread.run(Thread.java:748)
    "t1":
            at com.atguigu.interview.study.thread.HoldLockThread.run(DeadLockDemo.java:25)
            - waiting to lock <0x000000076b820ca8> (a java.lang.String)
            - locked <0x000000076b820c70> (a java.lang.String)
            at java.lang.Thread.run(Thread.java:748)

    Found 1 deadlock.
 */
class HoldLockThread implements Runnable {

    private String lockA;
    private String lockB;

    public HoldLockThread(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    @Override
    public void run() {
        synchronized (lockA) {
            System.out.println(Thread.currentThread().getName() + "\t自己持有：" + lockA + "，尝试获取：" + lockB);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "\t自己持有：" + lockB + "，尝试获取：" + lockA);
            }
        }
    }
}

public class DeadLockDemo {

    public static void main(String[] args) {

        String lockA = "lockA";
        String lockB = "lockB";

        new Thread(new HoldLockThread(lockA, lockB), "t1").start();
        new Thread(new HoldLockThread(lockB, lockA), "t2").start();
    }
}
