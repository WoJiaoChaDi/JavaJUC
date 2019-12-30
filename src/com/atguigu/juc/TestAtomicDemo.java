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
