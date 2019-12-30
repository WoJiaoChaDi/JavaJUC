package com.atguigu.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TestCallable {

    /*
     * 1.创建执行线程的方式三：出现Callable接口。相较于实现Runnable 接口的方式，方法可以有返回值，并且能够跑出异常
     * 2.执行 Callable 方法，需要FutureTask 实现类的支持，用于接收运算结果。 FutureTask 是 Future 接口的实现类
     * */
    public static void main(String[] args) {

        ThreadCallableDemo tcd = new ThreadCallableDemo();

        Integer sum = 0;
        for (int i = 0; i < 4; i++) {
            //1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果
            FutureTask<Integer> result = new FutureTask<>(tcd);
            new Thread(result).start();

            //2.接收线程运算后的结果，此处跟 闭锁 一样，只有线程完全运行完后才会执行 result.get() 方法
            try {
                Integer temp_sum = result.get();
                sum += temp_sum;
                System.out.println(sum);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
    }
}

class ThreadCallableDemo implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 101; i++) {
            sum += i;
        }
        Thread.sleep(2000);
        return sum;
    }
}

class ThreadRunnableDemo implements Runnable {
    @Override
    public void run() {

    }
}
