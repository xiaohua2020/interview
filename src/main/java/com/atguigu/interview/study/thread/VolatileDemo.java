package com.atguigu.interview.study.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
    JMM(Java内存模型Java Memory Model,简称JMM)本身是一种抽象的概念 并不真实存在,
    它描述的是一组规则或规范通过规范定制了程序中各个变量(包括实例字段,静态字段和构成数组对象的元素)的访问方式.

    JMM关于同步规定:
    1.线程解锁前,必须把共享变量的值刷新回主内存
    2.线程加锁前,必须读取主内存的最新值到自己的工作内存
    3.加锁解锁是同一把锁

    由于JVM运行程序的实体是线程,而每个线程创建时JVM都会为其创建一个工作内存(有些地方成为栈空间),工作内存是每个线程的私有数据区域,
    而Java内存模型中规定所有变量都存储在主内存,主内存是共享内存区域,所有线程都可访问,但线程对变量的操作(读取赋值等)必须在工作内存中进行,
    首先要将变量从主内存拷贝到自己的工作空间,然后对变量进行操作,操作完成再将变量写回主内存,不能直接操作主内存中的变量,
    各个线程中的工作内存储存着主内存中的变量副本拷贝,因此不同的线程无法访问对方的工作内存,此案成间的通讯(传值) 必须通过主内存来完成.
 */
class MyData { // MyData.java ===> MyData.class ===> JVM字节码
    // 共享变量
    volatile int number = 0;

    public void addTo60() {
        this.number = 60;
    }

    // 此时number前面已经加了volatile关键字修饰，volatile不保证原子性
    public void addPlusPlus() {
        this.number++; // 出现丢失写值的情况
        /*
           2: getfield      #2                  // Field number:I
           5: iconst_1
           6: iadd
           7: putfield      #2                  // Field number:I
         */
    }

    AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * AtomicInteger保证原子操作
     */
    public void addMyAtomic() {
        atomicInteger.getAndIncrement(); // 底层原理：CAS
    }
}

/**
 * 1. 验证volatile的可见性
 * 1.1 假如 int number = 0，number变量之前根本没有添加volatile关键字修饰，没有可见性
 * 1.2 添加了volatile，可以解决可见性问题
 * <p>
 * 2. 验证volatile不保证原子性
 * 2.1 原子性指的是什么意思？
 * <p>
 * 不可分割，完整性，也即某个线程正在做某个暗送具体业务时，中间不可以加塞或被分割，需要整体完整，
 * 要么同时成功，要么同时失败。
 * <p>
 * 2.2 volatile不保证原子性案例演示
 * <p>
 * 2.3 why
 * <p>
 * 2.4 如何解决原子性
 * - 加synchronized关键字
 * - 使用JUC下的AtomicInteger
 **/
public class VolatileDemo {

    public static void main(String[] args) {

        MyData data = new MyData();
        // 1 - 20号线程
        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    data.addPlusPlus();
                    data.addMyAtomic(); // 保证原子操作
                }
            }, String.valueOf(i)).start();
        }
        // 需要等待上面的20个线程全部计算完成后，再用main线程取得最终的结果值看是多少？20000
        while (Thread.activeCount() > 2) { // 默认后台有2个线程，1是main线程，2是后台gc线程
            // 多线程控制时间的方法
            Thread.yield(); // 线程礼让，暂停，等待上面20个线程计算完成
        }
        // 如果volatile可以保证原子性的话，输出的结果是20000
        System.out.println(Thread.currentThread().getName() + "\t int type，finally number value：" + data.number);
        System.out.println(Thread.currentThread().getName() + "\t AtomicInteger type，finally number value：" + data.atomicInteger);
    }

    /**
     * volatile可以保证可见性，及时通知其他线程，主物理内存的值已经被修改
     */
    public static void seeOkByVolatile() {
        MyData data = new MyData(); // 资源类

        // 第一个线程 AAA线程
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 相当于已经将60写回主内存，但main线程不可见，没有任何机制通知main主内存值已被修改
            data.addTo60();
            System.out.println(Thread.currentThread().getName() + "\t updated number value: " + data.number);
        }, "AAA").start();

        // 第二个线程 main线程
        while (data.number == 0) {
            // main线程一直等待循环，直到number值不再等于0
        }
        System.out.println(Thread.currentThread().getName() + "\t mission is over，main get number value：" + data.number);
    }
}
