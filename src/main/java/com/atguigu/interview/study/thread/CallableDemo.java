package com.atguigu.interview.study.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

class MyThread1 implements Runnable {
    @Override
    public void run() {

    }
}

class MyThread2 implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return null;
    }
}


class MyThread3 implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\tinvoked call");
        TimeUnit.SECONDS.sleep(3);
        return 1024;
    }
}

/*
    多线程中，第3种获取多线程的方式

    实现Runnable接口与实现Callable接口区别有3点：

    1. Runnable接口无返回值，Callable有返回值
    2. Runnable接口不会抛出异常，Callable会抛出异常
    3. 接口实现方法不一样，Runnable实现run()方法，Callable实现call()方法

    已经有Runnable接口，为什么还会出现Callable接口？

    车会开，你会修吗？
 */
public class CallableDemo {

    public static void main(String[] args) throws Exception {

        // FutureTask(Callable<V> callable)
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread3());
        FutureTask<Integer> futureTask2 = new FutureTask<>(new MyThread3());

        new Thread(futureTask, "AA").start();
        new Thread(futureTask2, "BB").start();
        // int result02 = futureTask.get();

        System.out.println(Thread.currentThread().getName() + "*********************");

        int result01 = 100;

        // while (!futureTask.isDone()) {
        //
        // }

        int result02 = futureTask.get(); // 建议放最后
        System.out.println("result:" + (result01 + result02));
    }
}