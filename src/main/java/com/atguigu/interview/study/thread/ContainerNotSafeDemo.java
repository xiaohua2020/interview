package com.atguigu.interview.study.thread;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/*
    优秀的Java工程师必不可少的方法论与思维方式（不是背题，而是方法论，软实力培养 ）

    练武不练功，到老一场空（武功没有高低之分，只有强弱之别）

    故障收集手册

    不伙子你是我最近面试的求职的程序员当中基础知识最扎实的一个，能够回答到这，挺不错的！！！

    那当然，你死定了（捧杀比谤杀更可恨）

    醒醒，加大电流该吃药了

    没有没有，经理我只不过是经理引导的好，走狗屎运，刚好经理问到的问题是我复习到或以前用到过的问题，我相信要是经理您要是再问我再深一点，
    再难一点的问题，我肯定也会挂的（以退为进）。

    笔试的意义不是筛选出人才，笔试的意义是过滤掉学渣

    不要只是会用，会用只不过是一个API调用工程师，底层原理

    java.util.Vector

    public synchronized boolean add(E e) {
        modCount++;
        ensureCapacityHelper(elementCount + 1);
        elementData[elementCount++] = e;
        return true;
    }

    java.util.concurrent.CopyOnWriteArrayList

    CopyOnWrite容器即写时复制的容器，往一个容器添加新元素的时候，不直接往当前容器Object[]
    添加，而是先将当前容器Object[]进行Copy，复制出一个新的容器Object[] newElements，然
    后新的容器Object[] newElements里添加元素，添加完元素之后，再将原容器的引用指向新的容
    器setArray(newElements);这样做的好处是可以对CopyOnWrite容器进行并发的读，而不是需要
    加锁，因为当前容器不会添加任何元素，所以CopyOnWrite容器也是一种读写分离的思想，读和写不
    同的容器。

    public class CopyOnWriteArrayList<E> implements List<E>, RandomAccess,
        Cloneable, java.io.Serializable {
        final transient ReentrantLock lock = new ReentrantLock();
        private transient volatile Object[] array;
    }

    public boolean add(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e;
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }

    1. 故障现象
       java.util.ConcurrentModificationException（Java并发修改异常）

    2. 导致原因
    并发争抢修改导致，参考我们的花名册签名情况
    一个人正在写入，另外一个同学过来抢夺，导致数据不一致异常。并发修改异常

    3. 解决方案
       不能回答加锁！！！不如回答使用Vector类解决
       3.1 List<String> list = new Vector<>();
       3.2 List<String> list = Collections.synchronizedList(new ArrayList<>());
       3.3 List<String> list = new CopyOnWriteArrayList<>();

    4. 优化建议（同样的错误不犯第2次）

    java.util.concurrent.CopyOnWriteArraySet

    public class CopyOnWriteArraySet<E> extends AbstractSet<E>
        implements java.io.Serializable {

        private final CopyOnWriteArrayList<E> al;

        public CopyOnWriteArraySet() {
            al = new CopyOnWriteArrayList<E>();
        }

        public boolean add(E e) {
            return al.addIfAbsent(e);
        }

        public boolean addIfAbsent(E e) {
            Object[] snapshot = getArray();
            return indexOf(e, snapshot, 0, snapshot.length) >= 0 ? false :
            addIfAbsent(e, snapshot);
        }

        private boolean addIfAbsent(E e, Object[] snapshot) {
            final ReentrantLock lock = this.lock;
            lock.lock();
            try {
                Object[] current = getArray();
                int len = current.length;
                if (snapshot != current) {
                    // Optimize for lost race to another addXXX operation
                    int common = Math.min(snapshot.length, len);
                    for (int i = 0; i < common; i++)
                        if (current[i] != snapshot[i] && eq(e, current[i]))
                            return false;
                    if (indexOf(e, current, common, len) >= 0)
                            return false;
                }
                Object[] newElements = Arrays.copyOf(current, len + 1);
                newElements[len] = e;
                setArray(newElements);
                return true;
            } finally {
                lock.unlock();
            }
        }
    }

    java.util.HashSet

    public class HashSet<E> extends AbstractSet<E>
        implements Set<E>, Cloneable, java.io.Serializable {

        private transient HashMap<E,Object> map;
        private static final Object PRESENT = new Object();

        public HashSet() {
            map = new HashMap<>();
        }

        public boolean add(E e) {
            return map.put(e, PRESENT)==null;
        }
    }

 */
public class ContainerNotSafeDemo {
    public static void main(String[] args) {
        // Map<String, String> map = new HashMap<>();
        // <String, String> map = new ConcurrentHashMap<>();
        Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }

    private static void setNotSafe() {
        // Set<String> set = new HashSet<>();
        // <String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }

        new HashSet<String>();
    }

    private static void listNotSafe() {
        // List<String> list = new ArrayList<>();
        // List<String> list = new Vector<>();
        // List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
}
