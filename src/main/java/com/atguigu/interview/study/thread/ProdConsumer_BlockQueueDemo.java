package com.atguigu.interview.study.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MyResource {

    private volatile boolean FLAG = true; // true默认开启，生产+消费
    private AtomicInteger atomicInteger = new AtomicInteger(); // 原子引用
    private BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    // 生产者
    public void producer() throws Exception {

        String data = null;
        boolean retValue;
        // 判断
        while (FLAG) {

            data = atomicInteger.incrementAndGet() + ""; // ++i
            retValue = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);

            if (retValue) {
                System.out.println(Thread.currentThread().getName() + "\t插入队列【" + data + "】成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "\t插入队列【" + data + "】成功");
            }
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println(Thread.currentThread().getName() + "大老板叫停，表示FLAG=false，生产动作结束");
    }

    // 消费者
    public void consumer() throws Exception {

        String result = null;

        while (FLAG) {
            result = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (null == result || result.equalsIgnoreCase("")) {
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "已经超过2秒没有获取到消息，消费退出");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t消费队列【" + result + "】成功");
        }
    }

    // 停止
    public void stop() {
        this.FLAG = false;
    }
}

/*
    永远传参，传接口
 */
public class ProdConsumer_BlockQueueDemo {

    public static void main(String[] args) {

        MyResource resource = new MyResource(new LinkedBlockingQueue<>(10));

        // 启动生产者线程
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "生产者线程启动");
            try {
                resource.producer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "producer").start();
        // 启动消费者线程
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "消费者线程启动");
            try {
                resource.consumer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "consumer").start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("5秒钟时间到，大老板叫停，活动结束！！！");
        resource.stop();
    }
}
