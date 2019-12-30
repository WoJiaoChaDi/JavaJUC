package com.atguigu.juc;

import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {

    /*
    * CountDownLatch：闭锁，在完成某些运算时，只有其他所有线程的运算全部完成，当前运算才继续执行。
    * */
    public static void main(String[] args) {

        final CountDownLatch latch = new CountDownLatch(10);

        long start = System.currentTimeMillis();
        LatchDemo latchDemo = new LatchDemo(latch);

        for (int i = 0; i < 10; i++) {
            new Thread(latchDemo).start();
        }

        //只有latch数减为0的时候，此能执行完成此条语句，不然无限等待下去
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("所有线程执行完成所需要时间：" + (end - start));
    }
}

class LatchDemo implements Runnable {

    private CountDownLatch latch;

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < 500; i++) {
                if (i % 2 == 0) {
                    System.out.println(i);
                }
            }
        }finally {
            //必须要执行
            latch.countDown();
        }
    }
}
