package com.atguigu.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * 一、线程池：提供了一个线程队列，队列中保存这所有等待状态的线程。避免了创建与销毁的额外开销，提高了响应速度。
 *
 * 二、线程池的体系结构：
 *       java.util.concurrent.Executor：负责线程的使用和调度的根接口
 *           |--ExecutorService：子接口  线程池的主要接口
 *               |--ThreadPoolExecutor 实现类
 *               |--ScheduledExecutorService 子接口：负责线程的调度
 *                   |--ScheduledThreadPoolExecutor ：继承了ThreadPoolExecutor，实现了ScheduledExecutorService接口
 * 三、工具类：Executor
 * ExecutorService newFixedThreadPool()  创建固定大小的线程池
 * ExecutorService newCachedThreadPool() 缓存线程池，线程池的数量不固定，可以根据需求自动的更改数量。
 * ExecutorService newSingleThreadExecutor() 创建单个线程池。线程池只有一个线程。
 *
 * ScheduledExecutorService newScheduledThreadPool   创建固定大小的线程，可以延迟或定时的执行任务。
 *
 * */
public class TestThreadPool {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1. 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        ThreadPoolDemo tpd = new ThreadPoolDemo();

        //2. 为线程池中的线程分配任务(Runnable)
        //for (int i = 0; i < 10; i++) {
        //    pool.submit(tpd);
        //}

        //2. 为线程池中的线程分配任务(Callable)
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = pool.submit(() -> {
                int sum = 0;
                for (int j = 0; j < 100; j++) {
                    sum += j;
                }
                return sum;
            });
            list.add(future.get());
        }

        System.out.println(list);

        //3. 关闭线程池
        pool.shutdown();//平和的方式关闭：等待现有的任务关闭
        //pool.shutdownNow();//立刻关闭，不管任务是否完成


    }
}

class ThreadPoolDemo implements Runnable {

    private int i = 0;

    @Override
    public void run() {
        while (i <= 100) {
            System.out.println(Thread.currentThread().getName() + " : " + i++);
        }
    }
}
