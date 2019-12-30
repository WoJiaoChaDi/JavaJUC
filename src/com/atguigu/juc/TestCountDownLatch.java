package com.atguigu.juc;

import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {

    /*
    * CountDownLatch：闭锁，在完成某些运算时，只有其他所有线程的运算全部完成，当前运算才继续执行。
    * */
    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        LatchDemo latchDemo = new LatchDemo();

        for (int i = 0; i < 10; i++) {
            new Thread(latchDemo).start();
        }
        long end = System.currentTimeMillis();

        System.out.println("所有线程执行完成所需要时间：" + (end - start));
    }
}

class LatchDemo implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 500; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }
}
