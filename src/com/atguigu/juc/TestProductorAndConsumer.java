package com.atguigu.juc;

public class TestProductorAndConsumer {
}

class Clerk {
    private int product = 0;

    //进货
    public synchronized void get(){
        if (product >= 10) {
            System.out.println("产品已满！");
        } else {
            System.out.println(Thread.currentThread().getName() + ":" + ++product);
        }
    }

    //卖货
    public synchronized void sale(){
        if (product <= 0) {
            System.out.println("缺货！");
        } else {
            System.out.println(Thread.currentThread().getName() + ":" + --product);
        }
    }
}

class Productor implements Runnable {

    private Clerk clerk;

    public Productor(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.get();
        }
    }
}

//消费者
class Consumer implements Runnable {
    private Clerk clerk;

    public Consumer(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            clerk.sale();
        }
    }
}