package com.atguigu.interview.study.thread;

import java.util.concurrent.*;

/*
    线程池做的工作主要是控制运行的线程的数量,处理过程中将任务加入队列,然后在线程创建后启动这些任务,如果线程超过了最大数量,
    超出的数量的线程排队等候,等其他线程执行完毕,再从队列中取出任务来执行.

    他的主要特点为:线程复用:控制最大并发数:管理线程.

    第一: 降低资源消耗.通过重复利用自己创建的线程降低线程创建和销毁造成的消耗.
    第二: 提高响应速度.当任务到达时,任务可以不需要等到线程创建就能立即执行.
    第三: 提高线程的可管理性.线程是稀缺资源,如果无限制的创建,不仅会消耗资源,还会降低系统的稳定性,使用线程池可以进行统一分配,调优和监控.

    第四种获得、使用Java多线程的方式，线程池


public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
 */
public class MyThreadPoolDemo {

    public static void main(String[] args) {

        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(),
                // new ThreadPoolExecutor.AbortPolicy()); // 默认拒绝策略，报异常
                new ThreadPoolExecutor.CallerRunsPolicy()); // 回退调用者，不报异常
        try {
            // 模拟10个用户输业务，每个用户就是一个来自外部的请求线程
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    public static void threadPoolInit() {
        // System.out.println(Runtime.getRuntime().availableProcessors()); // 查看cpu核数
        // ExecutorService threadPool = Executors.newFixedThreadPool(5); // 一池5个处理线程
        // ExecutorService threadPool = Executors.newSingleThreadExecutor(); // 一池1个处理线程
        ExecutorService threadPool = Executors.newCachedThreadPool(); // 一池多个处理线程
        // 了解
        // ExecutorService threadPool = Executors.newScheduledThreadPool(2); // 线程调度
        // ExecutorService threadPool = Executors.newWorkStealingPool(); // Java8新出

        try {
            // 模拟10个用户输业务，每个用户就是一个来自外部的请求线程
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t办理业务");
                });
                TimeUnit.SECONDS.sleep(3000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }
}
