package com.atguigu.interview.study.thread;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

@Getter
@ToString
@AllArgsConstructor
class User {
    String username;
    int age;
}

public class AtomicReferenceDemo {

    public static void main(String[] args) {

        User u1 = new User("张三", 22);
        User u2 = new User("李四", 25);

        AtomicReference<User> atomicReference = new AtomicReference<User>();
        atomicReference.set(u1);

        System.out.println(atomicReference.compareAndSet(u1, u2) + "\t" + atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(u1, u2) + "\t" + atomicReference.get().toString());
    }
}
