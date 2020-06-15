package com.atguigu.interview.study.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/*
    当阻塞队列是空时,从队列中获取元素的操作将会被阻塞.
    当阻塞队列是满时,往队列中添加元素的操作将会被阻塞.
    同样
    试图往已满的阻塞队列中添加新圆度的线程同样也会被阻塞,知道其他线程从队列中移除一个或者多个元素或者全清空队列后使队列重新变得空闲起来并后续新增.

    在多线程领域:所谓阻塞,在某些情况下会挂起线程(即线程阻塞),一旦条件满足,被挂起的线程优先被自动唤醒

    为什么需要使用BlockingQueue
    好处是我们不需要关心什么时候需要阻塞线程,什么时候需要唤醒线程,因为BlockingQueue都一手给你包办好了
    在concurrent包 发布以前,在多线程环境下,我们每个程序员都必须自己去控制这些细节,尤其还要兼顾效率和线程安全,而这会给我们的程序带来不小的复杂度.

    抛出异常
    add(e)、remove()、element()
    当阻塞队列满时,再往队列里面add插入元素会抛IllegalStateException: Queue full
    当阻塞队列空时,再往队列Remove元素时候回抛出NoSuchElementException

    特殊值
    offer(e)、poll()、peek()
    插入方法,成功返回true 失败返回false
    移除方法,成功返回元素,队列里面没有就返回null

    一直阻塞
    put(e)、take()
    当阻塞队列满时,生产者继续往队列里面put元素,队列会一直阻塞直到put数据or响应中断退出
    当阻塞队列空时,消费者试图从队列take元素,队列会一直阻塞消费者线程直到队列可用.

    超时退出
    offer(e, time, unit)、poll(time, unit)
    当阻塞队列满时,队列会阻塞生产者线程一定时间,超过后限时后生产者线程就会退出

    队列：先进先出
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws Exception {

        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        // 添加元素
        // System.out.println(blockingQueue.add("a"));
        // System.out.println(blockingQueue.add("b"));
        // System.out.println(blockingQueue.add("c"));
        // 当阻塞队列满时,再往队列里面add插入元素会抛IllegalStateException: Queue full
        // System.out.println(blockingQueue.add("x"));

        // 添加元素
        // System.out.println(blockingQueue.offer("a"));
        // System.out.println(blockingQueue.offer("b"));
        // System.out.println(blockingQueue.offer("c"));
        // 插入方法,成功返回true 失败返回false
        // System.out.println(blockingQueue.offer("x"));

        // 添加元素(带阻塞)
        // blockingQueue.put("aa");
        // blockingQueue.put("bb");
        // blockingQueue.put("cc");
        // 当阻塞队列满时,生产者继续往队列里面put元素,队列会一直阻塞直到put数据or响应中断退出
        // blockingQueue.put("dd");

        // 添加元素（超时）
        System.out.println(blockingQueue.offer("a", 2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("a", 2L, TimeUnit.SECONDS));
        System.out.println(blockingQueue.offer("a", 2L, TimeUnit.SECONDS));
        // 当阻塞队列满时,队列会阻塞生产者线程一定时间,超过后限时后生产者线程就会退出(返回false)
        System.out.println(blockingQueue.offer("a", 2L, TimeUnit.SECONDS));

        // 获取元素(带阻塞)
        // System.out.println(blockingQueue.take());
        // System.out.println(blockingQueue.take());
        // System.out.println(blockingQueue.take());
        // 当阻塞队列空时,消费者试图从队列take元素,队列会一直阻塞消费者线程直到队列可用.
        // System.out.println(blockingQueue.take());

        // 删除元素
        // System.out.println(blockingQueue.remove());
        // System.out.println(blockingQueue.remove());
        // System.out.println(blockingQueue.remove());
        // 当阻塞队列空时,再往队列Remove元素时候回抛出NoSuchElementException
        // System.out.println(blockingQueue.remove());

        // 删除元素
        // System.out.println(blockingQueue.poll());
        // System.out.println(blockingQueue.poll());
        // System.out.println(blockingQueue.poll());
        // 移除方法,成功返回元素,队列里面没有就返回null
        // System.out.println(blockingQueue.poll());

        // 检查元素（队首元素）
        // System.out.println(blockingQueue.element());

        // 检查元素（探测队首元素）
        // System.out.println(blockingQueue.peek());
    }
}
