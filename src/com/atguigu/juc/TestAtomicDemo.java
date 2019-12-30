package com.atguigu.juc;

public class TestAtomicDemo {

    /*
    * 1. i++ 的原子性问题： i++的操作分为三个步骤的“读-改-写”
    *       int i =10;
    *       i = i++;    //10
    *
    *       int temp = i;
    *       i = i + 1;
    *       i = temp;
    *
    *
    * */
    public static void main(String[] args) {

        AtomicDemo ad = new AtomicDemo();
        for (int i = 0; i < 10; i++) {
            new Thread(ad).start();
        }
    }
}

class AtomicDemo implements Runnable{

    //对于原子性问题,volatile也无能为力
    //因为线程里面对serialNumber有读-写操作，在一个线程写之前，另一个线程读取了旧数据，则两个线程写的时候会互相覆盖，导致无法保持原子性，及时他们都在主存中操作，也会有先后写覆盖的问题
    //private volatile int serialNumber = 0;
    private int serialNumber = 0;

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + ":" + getSerialNumber());
    }

    public int getSerialNumber(){
        return serialNumber++;
    }
}
