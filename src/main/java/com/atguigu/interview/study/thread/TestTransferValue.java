package com.atguigu.interview.study.thread;

public class TestTransferValue {

    public void changeValue1(int age) {
        age = 30;
    }

    public void changeValue2(Person person) {
        person.setPersonName("xxx");
    }

    public void changeValue3(String string) {
        string = "xxx";
    }

    public static void main(String[] args) {

        TestTransferValue test = new TestTransferValue();

        int age = 20;
        test.changeValue1(age); // 20
        System.out.println("age：" + age);

        Person person = new Person("abc");
        test.changeValue2(person); // xxx
        System.out.println("personName：" + person.getPersonName());

        String str = "abc"; // abc
        test.changeValue3(str);
        System.out.println("String：" + str);
    }
}
