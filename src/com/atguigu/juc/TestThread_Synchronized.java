package com.atguigu.juc;

import org.junit.Test;

public class TestThread_Synchronized{

    //***
    //***
    //***同步代码块，只影响使用同一个对象(this = 调用该方法的对象）的线程 的同步代码块 内容的资源争抢
    public void getOne(){

        System.out.println(Thread.currentThread().getName() + ":one_进入方法 ");

        //同步代码块(this) 和 同步方法 是对资源的争抢上面同级别的
        synchronized (this){
            try {
                System.out.println(Thread.currentThread().getName() + ":one锁住对象 ");
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":one释放对象 ");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        System.out.println(Thread.currentThread().getName() + ":one_退出方法 ");
    }

    //同步代码块(this) 和 同步方法 是对资源的争抢上面同级别的
    public synchronized void getTwo(){
        System.out.println(Thread.currentThread().getName() + ":two_进入方法 ");

        try {
            System.out.println(Thread.currentThread().getName() + ":two_锁住对象 ");
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":two_释放对象 ");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        System.out.println(Thread.currentThread().getName() + ":two_退出方法 ");
    }

    @Test
    public void test1() throws InterruptedException {
        /*
        输出：
            Thread-0:one_进入方法
            Thread-0:one锁住对象
            Thread-0:0
            Thread-1:one_进入方法
            Thread-0:1
            Thread-0:2
            Thread-0:3
            Thread-0:4
-1            Thread-0:one释放对象
            Thread-3:two_进入方法
            Thread-3:two_锁住对象
            Thread-3:0
            Thread-3:1
-2            Thread-0:one_退出方法
            Thread-3:2
            Thread-3:3
            Thread-3:4
            Thread-3:two_释放对象
            Thread-3:two_退出方法
            Thread-2:two_进入方法
            Thread-2:two_锁住对象
            Thread-2:0
            Thread-2:1
            Thread-2:2
            Thread-2:3
            Thread-2:4
            Thread-2:two_释放对象
            Thread-2:two_退出方法
            Thread-1:one锁住对象
            Thread-1:0
            Thread-1:1
            Thread-1:2
            Thread-1:3
            Thread-1:4
            Thread-1:one释放对象
            Thread-1:one_退出方法
            --------number
        */
        // 代码块锁的是this，单个对象，
        // 普通同步方法锁的也是this，单个对象
        // 所以使用同一个对象时，多个（同步代码块(this) 和 同步方法）会互相争抢资源
        // 同步代码块(this) 和 同步方法 是对资源的争抢上面同级别的
        // 同步代码块需要注意的是：-1、-2之间被争抢了锁，因为同步代码块只同步  代码块中的内容，执行完便放开了
        TestThread_Synchronized number = new TestThread_Synchronized();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number.getOne()).start();
        new Thread( () -> number.getTwo()).start();
        new Thread( () -> number.getTwo()).start();

        //此处等待是因为junit执行完会杀掉所有线程，此处多等待一会儿是为了等所有线程执行完毕
        Thread.sleep(30000);
        System.out.println("--------number");
    }

    @Test
    public void test2() throws InterruptedException {
        /*
        * 输出
            Thread-0:one_进入方法
            Thread-0:one锁住对象
            Thread-1:one_进入方法
            Thread-1:one锁住对象
            Thread-0:0
            Thread-1:0
            Thread-2:two_进入方法
            Thread-2:two_锁住对象
            Thread-2:0
            Thread-3:two_进入方法
            Thread-3:two_锁住对象
            Thread-3:0
            Thread-2:1
            Thread-0:1
            Thread-1:1
            Thread-3:1
            ---四个线程都已开启---
            Thread-0:2
            Thread-2:2
            Thread-1:2
            Thread-3:2
            Thread-1:3
            Thread-0:3
            Thread-2:3
            Thread-3:3
            Thread-1:4
            Thread-0:4
            Thread-2:4
            Thread-3:4
            Thread-1:one释放对象
            Thread-0:one释放对象
            Thread-2:two_释放对象
            Thread-3:two_释放对象
            Thread-1:one_退出方法
            Thread-0:one_退出方法
            Thread-2:two_退出方法
            Thread-3:two_退出方法
            --------number3456
        */
        // 同步代码块(this) 和 synchronized方法 锁的是this对象(number3\number4\number5\number6)，即单个对象，
        // 此例中是4各不同的对象，所以多个对象互不影响
        TestThread_Synchronized number3 = new TestThread_Synchronized();
        TestThread_Synchronized number4 = new TestThread_Synchronized();
        TestThread_Synchronized number5 = new TestThread_Synchronized();
        TestThread_Synchronized number6 = new TestThread_Synchronized();
        new Thread( () -> number3.getOne()).start();
        new Thread( () -> number4.getOne()).start();
        new Thread( () -> number5.getTwo()).start();
        new Thread( () -> number6.getTwo()).start();
        Thread.sleep(1000);
        System.out.println("---四个线程都已开启---");

        //此处等待是因为junit执行完会杀掉所有线程，此处多等待一会儿是为了等所有线程执行完毕
        Thread.sleep(30000);
        System.out.println("--------number3456");
    }




    //***
    //***
    //***同步代码块，只影响使用同一个对象（传入的obj对象）的线程 的同步代码块 内容的资源争抢
    public void getOneLockObj(Object obj){

        System.out.println(Thread.currentThread().getName() + ":OneLockObj_进入方法 ");

        synchronized (obj){
            try {
                System.out.println(Thread.currentThread().getName() + ":OneLockObj_锁住对象 " + obj);
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":OneLockObj_释放对象 " + obj);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        System.out.println(Thread.currentThread().getName() + ":OneLockObj_退出方法 ");
    }

    public synchronized void getTwoLockObj(Object obj){
        System.out.println(Thread.currentThread().getName() + ":TwoLockObj_进入方法 ");

        try {
            System.out.println(Thread.currentThread().getName() + ":TwoLockObj锁住对象 " + obj);
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":TwoLockObj释放对象 " + obj);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        System.out.println(Thread.currentThread().getName() + ":TwoLockObj_退出方法 ");
    }

    @Test
    public void test3() throws InterruptedException {
        Object obj = new Object();

        /*
            Thread-0:OneLockObj_进入方法
            Thread-0:OneLockObj_锁住对象 java.lang.Object@77032f77
            Thread-0:0
            Thread-1:OneLockObj_进入方法
            Thread-2:TwoLockObj_进入方法
            Thread-2:TwoLockObj锁住对象 java.lang.Object@77032f77
            Thread-2:0
            Thread-0:1
            Thread-2:1
            Thread-0:2
            Thread-2:2
            Thread-0:3
            Thread-2:3
            Thread-0:4
            Thread-2:4
-1            Thread-0:OneLockObj_释放对象 java.lang.Object@77032f77
-2            Thread-1:OneLockObj_锁住对象 java.lang.Object@77032f77
            Thread-1:0
-3            Thread-2:TwoLockObj释放对象 java.lang.Object@77032f77
            Thread-0:OneLockObj_退出方法
            Thread-1:1
-4            Thread-2:TwoLockObj_退出方法
-5            Thread-3:TwoLockObj_进入方法
-6            Thread-3:TwoLockObj锁住对象 java.lang.Object@77032f77
            Thread-3:0
            Thread-1:2
            Thread-3:1
            Thread-1:3
            Thread-3:2
            Thread-1:4
            Thread-3:3
            Thread-1:OneLockObj_释放对象 java.lang.Object@77032f77
            Thread-3:4
            Thread-1:OneLockObj_退出方法
            Thread-3:TwoLockObj释放对象 java.lang.Object@77032f77
            Thread-3:TwoLockObj_退出方法
            --------obj
        * */
        //前两个线程，锁定的obj 是同一个对象，所以前两个同步代码块会互相争抢资源
        //后两个线程，由于getTwoLockObj是普通同步方法，锁的是同一个this(number_obj)对象，所以也会互相抢占资源，但是不会跟上面抢占资源
        //同时注意，同步代码块只 只同步代码块内的内容，而 同步方法 是同步一整个方法
        //      可以通过 -1、-2行的代码看出，同步代码块一放开锁，马上就另一个线程被锁上了
        //      而同步方法，-3、-4、-5、-6可以看出，是整个方法结束后，才真正的把锁方法
        TestThread_Synchronized number_obj = new TestThread_Synchronized();
        new Thread( () -> number_obj.getOneLockObj(obj)).start();
        new Thread( () -> number_obj.getOneLockObj(obj)).start();
        new Thread( () -> number_obj.getTwoLockObj(obj)).start();
        new Thread( () -> number_obj.getTwoLockObj(obj)).start();

        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        Thread.sleep(30000);
        System.out.println("--------obj");
    }

    @Test
    public void test4() throws InterruptedException {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object obj3 = new Object();
        Object obj4 = new Object();

        /*
            Thread-0:OneLockObj_进入方法
            Thread-0:OneLockObj_锁住对象 java.lang.Object@1dc2d142
            Thread-0:0
            Thread-1:OneLockObj_进入方法
            Thread-1:OneLockObj_锁住对象 java.lang.Object@1ded38e6
            Thread-1:0
            Thread-2:TwoLockObj_进入方法
            Thread-2:TwoLockObj锁住对象 java.lang.Object@79a6e4c6
            Thread-2:0
            Thread-1:1
            Thread-0:1
            Thread-2:1
            Thread-1:2
            Thread-0:2
            Thread-2:2
            Thread-0:3
            Thread-1:3
            Thread-2:3
            Thread-1:4
            Thread-0:4
            Thread-2:4
            Thread-1:OneLockObj_释放对象 java.lang.Object@1ded38e6
            Thread-0:OneLockObj_释放对象 java.lang.Object@1dc2d142
            Thread-2:TwoLockObj释放对象 java.lang.Object@79a6e4c6
            Thread-0:OneLockObj_退出方法
            Thread-1:OneLockObj_退出方法
            Thread-2:TwoLockObj_退出方法
            Thread-3:TwoLockObj_进入方法
            Thread-3:TwoLockObj锁住对象 java.lang.Object@68199069
            Thread-3:0
            Thread-3:1
            Thread-3:2
            Thread-3:3
            Thread-3:4
            Thread-3:TwoLockObj释放对象 java.lang.Object@68199069
            Thread-3:TwoLockObj_退出方法
            --------obj123
        * */
        //对于obj1、obj2，同步代码块 锁定的是各自的obj1、obj2两个不同的对象，所以两个线程的同步代码块不会互相影响
        //对于obj3、obj4，由于 getTwoLockObj 是 普通同步方法，锁的是同一个this(number_obj123)对象，所以会互相争抢资源
        TestThread_Synchronized number_obj123 = new TestThread_Synchronized();
        new Thread( () -> number_obj123.getOneLockObj(obj1)).start();
        new Thread( () -> number_obj123.getOneLockObj(obj2)).start();
        new Thread( () -> number_obj123.getTwoLockObj(obj3)).start();
        new Thread( () -> number_obj123.getTwoLockObj(obj4)).start();

        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        Thread.sleep(30000);
        System.out.println("--------obj123");
    }





    //***
    //***
    //*** 同步代码块，锁住整个类（TestThread_Synchronized.class），只要有这个类存在，都会锁住，所以跟锁住this几乎一样
    public void getOneLockClass(){

        System.out.println(Thread.currentThread().getName() + ":OneLockClass_进入方法 ");

        synchronized (TestThread_Synchronized.class){
            try {
                System.out.println(Thread.currentThread().getName() + ":OneLockClass锁住对象 ");
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + ":OneLockClass释放对象 ");
        }

        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        System.out.println(Thread.currentThread().getName() + ":OneLockClass_退出方法 ");
    }

    public static synchronized void getTwoLockClass(){

        System.out.println(Thread.currentThread().getName() + ":static TwoLockClass_进入方法 ");

        try {
            System.out.println(Thread.currentThread().getName() + ":static TwoLockClass锁住对象 ");
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + ":static TwoLockClass释放对象 ");

        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        System.out.println(Thread.currentThread().getName() + ":static TwoLockClass_退出方法 ");
    }

    @Test
    public void test5() throws InterruptedException {
        /*
        输出：
            Thread-0:OneLockClass_进入方法
            Thread-0:OneLockClass锁住对象
            Thread-0:0
            Thread-1:OneLockClass_进入方法
            Thread-0:1
            Thread-0:2
            Thread-0:3
            Thread-0:4
            Thread-0:OneLockClass释放对象
            Thread-3:static TwoLockClass_进入方法
            Thread-3:static TwoLockClass锁住对象
            Thread-3:0
            Thread-0:OneLockClass_退出方法
            Thread-3:1
            Thread-3:2
            Thread-3:3
            Thread-3:4
            Thread-3:static TwoLockClass释放对象
            Thread-3:static TwoLockClass_退出方法
            Thread-2:static TwoLockClass_进入方法
            Thread-2:static TwoLockClass锁住对象
            Thread-2:0
            Thread-2:1
            Thread-2:2
            Thread-2:3
            Thread-2:4
            Thread-2:static TwoLockClass释放对象
            Thread-2:static TwoLockClass_退出方法
            Thread-1:OneLockClass锁住对象
            Thread-1:0
            Thread-1:1
            Thread-1:2
            Thread-1:3
            Thread-1:4
            Thread-1:OneLockClass释放对象
            Thread-1:OneLockClass_退出方法
            --------class
        */
        // getOneLockClass 锁定的是 TestThread_Synchronized.class 这个类
        // getTwoLockClass 锁定的是 this.class 这个类，即就是该方法所在的类，其实就是 TestThread_Synchronized.class 这个类
        // 所以因为是同一个类，所以四个方法会互相争抢资源
        // 这里需要注意的是，同步代码块只同步 代码块中的内容，一走出代码块，其他线程便可以争抢资源
        TestThread_Synchronized number_class = new TestThread_Synchronized();
        new Thread( () -> number_class.getOneLockClass()).start();
        new Thread( () -> number_class.getOneLockClass()).start();
        new Thread( () -> number_class.getTwoLockClass()).start();
        new Thread( () -> number_class.getTwoLockClass()).start();

        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        Thread.sleep(30000);
        System.out.println("--------class");
    }

    @Test
    public void test6() throws InterruptedException {
        //因为锁定的是Number_10.class这个类，所以会影响同步代码块中的内容
        /*
        * 输出
            Thread-3:one_0
            Thread-3:锁住对象
            Thread-4:one_0
            Thread-5:one_0
            Thread-3:释放对象
            Thread-3:one_2
            Thread-5:锁住对象
            Thread-5:释放对象
            Thread-5:one_2
            Thread-4:锁住对象
            Thread-4:释放对象
            Thread-4:one_2
        */
        TestThread_Synchronized number_class6 = new TestThread_Synchronized();
        TestThread_Synchronized number_class7 = new TestThread_Synchronized();
        TestThread_Synchronized number_class8 = new TestThread_Synchronized();
        TestThread_Synchronized number_class9 = new TestThread_Synchronized();
        new Thread( () -> number_class6.getOneLockClass()).start();
        new Thread( () -> number_class7.getOneLockClass()).start();
        new Thread( () -> number_class8.getTwoLockClass()).start();
        new Thread( () -> number_class9.getTwoLockClass()).start();

        //因为锁定的是this这个对象，所以同步代码块外的不受影响
        Thread.sleep(30000);
        System.out.println("--------class678");
    }

}
