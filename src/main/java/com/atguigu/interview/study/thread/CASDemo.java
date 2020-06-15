package com.atguigu.interview.study.thread;

import java.util.concurrent.atomic.AtomicInteger;

/*
    CAS（比较并交换） ---> CompareAndSet

    CAS的全称为Compare-And-Swap ,它是一条CPU并发原语.
    它的功能是判断内存某个位置的值是否为预期值,如果是则更新为新的值,这个过程是原子的.

    CAS并发原语提现在Java语言中就是sun.misc.UnSafe类中的各个方法.调用UnSafe类中的CAS方法,JVM会帮我实现CAS汇编指令.
    这是一种完全依赖于硬件 功能,通过它实现了原子操作,再次强调,由于CAS是一种系统原语,原语属于操作系统用于范畴,是由若干条指令组成,
    用于完成某个功能的一个过程,并且原语的执行必须是连续的,在执行过程中不允许中断,也即是说CAS是一条原子指令,
    不会造成所谓的数据不一致的问题（线程安全）.

    如果线程的期望值与主物理内存（主内存）的真实值一样，就修改为更新值，本次操作为true
    如果线程的期望值与主物理内存（主内存）的真实值不一样，不修改更新值，本次操作为false

    期望值与真实值相同，可以修改
    期望值与真实值不相同，不可以修改

    第一层境界：
    知道会用，API工程师

    第二层境界：
    底层原理：1. 自旋锁；2. unsafe类

    ------------------------------
    源码解读：java.util.concurrent.atomic.AtomicInteger
    ------------------------------

    public class AtomicInteger extends Number implements java.io.Serializable {
    private static final long serialVersionUID = 6214790243416807050L;

    // setup to use Unsafe.compareAndSwapInt for updates
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset; // 内存地址偏移量

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private volatile int value; // 保证可见性

    1.UnSafe
    是CAS的核心类 由于Java 方法无法直接访问底层 ,需要通过本地(native)方法来访问,UnSafe相当于一个后面,
    基于该类可以直接操作特额定的内存数据.UnSafe类在于sun.misc包中,其内部方法操作可以向C的指针一样直接操
    作内存,因为Java中CAS操作的助兴依赖于UNSafe类的方法.

    注意UnSafe类中所有的方法都是native修饰的,也就是说UnSafe类中的方法都是直接调用操作底层资源执行响应的任务

    2.变量ValueOffset,便是该变量在内存中的偏移地址,因为UnSafe就是根据内存偏移地址获取数据的

    为什么用这一个方法可以不加synchronized关键字也能保证原子性？

    public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, valueOffset, 1);
    }
    3.变量value和volatile修饰,保证了多线程之间的可见性.

    当前仅靠getAndIncrement()这一个方法解决i++在多线程环境下面线程安全的问题，
    unsafe.getAndAddInt(this, valueOffset, 1);
    第一个参数：this表示当前对象
    第二个参数：valueOffset当前这个对象的内存内存地址偏移量
    （这个对象的内存地址底层调用的是CAS思想，如果比较成功加1，如果比较失败，再重新获得，再比较一次，直至成功为止）
    第三个参数：初始值，加1。即当前这个对象的内存地址值是多少。

    ------------------------------
    源码解读：sun.misc.Unsafe
    ------------------------------
    unsafe.getAndAddInt(this, valueOffset, 1)

    var1 AtomicInteger对象本身.
    var2 该对象值的引用地址
    var4 需要变动的数值
    var5 是用过var1 var2找出内存中绅士的值
    用该对象当前的值与var5比较
    如果相同,更新var5的值并且返回true
    如果不同,继续取值然后比较,直到更新完成

    假设线程A和线程B两个线程同时执行getAndAddInt操作(分别在不同的CPU上):
    1.AtomicInteger里面的value原始值为3,即主内存中AtomicInteger的value为3,根据JMM模型,线程A和线程B各自持有一份值为3的value的副本分别到各自的工作内存.
    2.线程A通过getIntVolatile(var1,var2) 拿到value值3,这是线程A被挂起.
    3.线程B也通过getIntVolatile(var1,var2) 拿到value值3,此时刚好线程B没有被挂起并执行compareAndSwapInt方法比较内存中的值也是3 成功修改内存的值为4 线程B打完收工 一切OK.
    4.这是线程A恢复,执行compareAndSwapInt方法比较,发现自己手里的数值和内存中的数字4不一致,说明该值已经被其他线程抢先一步修改了,那A线程修改失败,只能重新来一遍了.
    5.线程A重新获取value值,因为变量value是volatile修饰,所以其他线程对他的修改,线程A总是能够看到,线程A继续执行compareAndSwapInt方法进行比较替换,直到成功.

    public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            // var1表示当前对象this，var2表示valueOffset
            // 1. 主内存得到一个值var5，拷贝至自己线程工作内存中
            // 2. 比较并交换，如果var1（当前对象），var2这个对象的内存地址的值与得到的值var5（快照的值，工作内存的值）是相同的，就修改
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

        return var5;
    }
    ------------------------------
    CAS缺点：

    1. 循环时间长开销很大、
    1.1. 我们可以看到getAndAddInt方法执行时，有个do while
    public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

        return var5;
    }
    如果CAS失败，会一直进行尝试，如果CAS长时间一直不成功，可能会给CPU带来很大的开销。
    2. 只能保证一个共享变量的原子性
    2.1. 当对一个共享变量执行操作时，我们可以使用循环CAS的方式来保证原子操作，但是对多个
    变量操作时，循环CAS就无法保证操作的原子性，这个时候就可以用锁来保证原子性
    3. 引出来ABA问题

    AtomicInteger

    CAS ---> Unsafe ---> CAS底层思想 ---> ABA ---> 原子引用更新 ---> 如何规避ABA问题

    ABA：狸猫换太子
    1、可以发现，CAS实现的过程是先取出内存中某时刻的数据，在下一时刻比较并替换，那么在这个时间差会导致数据的变化，此时就会导致出现“ABA”问题。
    2、什么是”ABA”问题
    比如说一个线程one从内存位置V中取出A，这时候另一个线程two也从内存中取出A，并且two进行了一些操作变成了B，然后two又将V位置的数据变成A，
    这时候线程one进行CAS操作发现内存中仍然是A，然后one操作成功。

    尽管线程one的CAS操作成功，但是不代表这个过程就是没有问题的。

    原子引用（AtomicReference）：

    解决ABA问题：理解原子引用 + 新增一种机制，那就是修改版本号（类似时间戳）

           线程名称     主内存值     版本号     修改值     版本号      修改值     版本号
    线程1     T1         100         1                             2019      2
    线程2     T2         100         1        101        2         100       3


 */
public class CASDemo {

    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.compareAndSet(5, 2019) + "\t current data：" + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 1024) + "\t current data：" + atomicInteger.get());

        atomicInteger.getAndIncrement();

    }
}


