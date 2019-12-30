package com.atguigu.juc;
/*
* 线程8锁
*
* 题目：判断打印的是“one” or “two”
*
* 1. 两个普通同步方法，同一个对象，两个线程，打印： // one two
* 2. 两个普通同步方法，同一个对象，两个线程，在getOne()中添加Thread.sleep(3000)，打印： // (睡3秒) one two
* 3. 两个普通同步方法，同一个对象，三个线程，新增普通方法getThree()，打印  //  three (睡3秒) one two
* 4. 两个普通同步方法，两个对象，两个线程，打印： // two (睡3秒) one
* 5. 一个静态同步方法，一个普通同步方法，同一个对象，两个线程，打印  // two (睡3秒) one
* 6. 两个静态同步方法，同一个对象，两个线程，打印  // (睡3秒) one two
* 7. 一个静态同步方法，一个普通同步方法，两个对象，两个线程  // two (睡3秒) one
* 8. 两个静态同步方法，两个对象，两个线程  // one (睡3秒) one
*
* 线程八锁的关键：
* 1.非静态方法的锁默认为 this 对象， 静态方法的锁为 对应的Class 实例
* 2.某一个时刻内，只能有一个线程持有锁，无论几个方法。
* */
public class TestThread_8_Monitor {
}

//1. 两个普通同步方法，同一个对象，两个线程，打印： // one two
class Number_1{

    public synchronized void getOne() {
        System.out.println("one");
    }

    public synchronized void getTwo() {
        System.out.println("two");
    }

    //输出：one two
    // synchronized在方法上，锁的是this这个对象，即number这个对象，所以先获取锁的占有锁，后来的只有等待
    public static void main(String[] args) {
        Number_1 number = new Number_1();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number.getTwo()).start();
    }
}

//2. 两个普通同步方法，同一个对象，两个线程，在getOne()中添加Thread.sleep(3000)，打印： // (睡3秒) one two
class Number_2{

    public synchronized void getOne() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public synchronized void getTwo() {
        System.out.println("two");
    }

    //输出：(等待3秒) one two
    //synchronized在方法上，锁的是this这个对象，即number这个对象，所以先获取锁的占有锁，后来的只有等待
    public static void main(String[] args) {
        Number_2 number = new Number_2();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number.getTwo()).start();
    }
}

//3. 两个普通同步方法，同一个对象，三个线程，新增普通方法getThree()，打印  //  three (睡3秒) one two
class Number_3{

    public synchronized void getOne() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public synchronized void getTwo() {
        System.out.println("two");
    }

    public void getThree(){
        System.out.println("three");
    }

    //输出：three (等待3秒) one two
    //synchronized在方法上，锁的是this这个对象的同步方法，即number这个对象的同步方法，所以先获取锁的占有锁，后来的只有等待
    public static void main(String[] args) {
        Number_3 number = new Number_3();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number.getTwo()).start();
        new Thread( () -> number.getThree()).start();
    }
}

//4. 两个普通同步方法，两个对象，两个线程，打印： // two (睡3秒) one
class Number_4{

    public synchronized void getOne() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public synchronized void getTwo() {
        System.out.println("two");
    }

    //输出：two (等待3秒) one
    //synchronized在方法上，锁的是this这个对象，即number这个对象，所以先获取锁的占有锁，后来的只有等待
    //所以两个不同对象，则不会互相阻塞
    public static void main(String[] args) {
        Number_4 number = new Number_4();
        Number_4 number2 = new Number_4();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number2.getTwo()).start();
    }
}

//5. 一个静态同步方法，一个普通同步方法，同一个对象，两个线程，打印  // two (睡3秒) one
class Number_5{

    public static synchronized void getOne() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public synchronized void getTwo() {
        System.out.println("two");
    }

    //输出：two (等待3秒) one
    //synchronized在方法上，锁的是this这个对象，即number这个对象，所以先获取锁的占有锁，后来的只有等待
    //synchronized在静态方法上，锁的是Number.class这个类对象，跟this对象不冲突，但是有且只有一个Number.class对象
    //所以两个不同对象，则不会互相阻塞
    public static void main(String[] args) {
        Number_5 number = new Number_5();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number.getTwo()).start();
    }
}

//6. 两个静态同步方法，同一个对象，两个线程，打印  // (睡3秒) one two
class Number_6{

    public static synchronized void getOne() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public static synchronized void getTwo() {
        System.out.println("two");
    }

    //输出：(等待3秒) one two
    //synchronized在方法上，锁的是this这个对象，即number这个对象，所以先获取锁的占有锁，后来的只有等待
    //synchronized在静态方法上，锁的是Number.class这个类对象，跟this对象不冲突，但是有且只有一个Number.class对象
    //是同一个class对象，所以会阻塞
    public static void main(String[] args) {
        Number_6 number = new Number_6();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number.getTwo()).start();
    }
}

//7. 一个静态同步方法，一个普通同步方法，两个对象，两个线程  // two (睡3秒) one
class Number_7{

    public static synchronized void getOne() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public synchronized void getTwo() {
        System.out.println("two");
    }

    //输出：two (等待3秒) one
    //synchronized在方法上，锁的是this这个对象，即number这个对象，所以先获取锁的占有锁，后来的只有等待
    //synchronized在静态方法上，锁的是Number.class这个类对象，跟this对象不冲突，但是有且只有一个Number.class对象
    //一个是class对象，一个是number2对象，所以不会阻塞
    public static void main(String[] args) {
        Number_7 number = new Number_7();
        Number_7 number2 = new Number_7();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number2.getTwo()).start();
    }
}

//8. 两个静态同步方法，两个对象，两个线程  // one (睡3秒) one
class Number_8{

    public static synchronized void getOne() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one");
    }

    public static synchronized void getTwo() {
        System.out.println("two");
    }

    //输出：(等待3秒) one two
    //synchronized在方法上，锁的是this这个对象，即number这个对象，所以先获取锁的占有锁，后来的只有等待
    //synchronized在静态方法上，锁的是Number.class这个类对象，跟this对象不冲突，但是有且只有一个Number.class对象
    //是同一个class队列，所以不会阻塞
    public static void main(String[] args) {
        Number_8 number = new Number_8();
        Number_8 number2 = new Number_8();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number2.getTwo()).start();
    }
}