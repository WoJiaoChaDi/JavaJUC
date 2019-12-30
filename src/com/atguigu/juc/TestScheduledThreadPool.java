package com.atguigu.juc;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/*
* 线程调度
* */
public class TestScheduledThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

        for (int i = 0; i < 10; i++) {
            //ScheduledFuture<Integer> future = pool.schedule(new Callable<Integer>() {
            //    @Override
            //    public Integer call() throws Exception {
            //        int num = new Random().nextInt(100);
            //        System.out.println(Thread.currentThread().getName() + " : " + num);
            //        return num;
            //    }
            //}, 2, TimeUnit.SECONDS);

            //线程池写法 Callable lambda表达式写法
            ScheduledFuture<Integer> future = pool.schedule(() -> {
                    int num = new Random().nextInt(100);
                    System.out.println(Thread.currentThread().getName() + " : " + num);
                    return num;
            }, 2, TimeUnit.SECONDS);//定义延迟时间和延迟单位

            System.out.println(future.get());
        }

        pool.shutdown();
    }
}
