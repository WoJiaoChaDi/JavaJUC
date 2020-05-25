package com.atguigu.juc;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class TestForkJoinPool {

    public static void main(String[] args) {
        Instant start = Instant.now();

        //ForkJoinPool线程池
        ForkJoinPool pool = new ForkJoinPool();
        //总任务
        ForkJoinTask<Long> task = new ForkJoinSumCalculate(0L, 10000000000L);
        Long sum = pool.invoke(task);
        System.out.println(sum);

        Instant end = Instant.now();

        //消耗时间为：2914
        System.out.println("消耗时间为：" + Duration.between(start, end).toMillis());
    }

    //普通的for循环
    @Test
    public void test1(){
        Instant start = Instant.now();

        long sum = 0L;
        for (long i = 0L; i < 10000000000L; i++) {
            sum += i;
        }
        System.out.println(sum);

        Instant end = Instant.now();

        //消耗时间为：5264
        System.out.println("消耗时间为：" + Duration.between(start, end).toMillis());
    }

    //Java8 新特性
    @Test
    public void test2(){
        Instant start = Instant.now();

        Long sum = LongStream.rangeClosed(0L, 10000000000L)
                                .parallel()
                                .reduce(0l, Long::sum);
        System.out.println(sum);

        Instant end = Instant.now();

        //消耗时间为：2264
        System.out.println("消耗时间为：" + Duration.between(start, end).toMillis());
    }
}

//RecursiveTask<T> 和 RecursiveActive  区别就是有没有返回值
class ForkJoinSumCalculate extends RecursiveTask<Long> {

    private long start;
    private long end;

    //临界值
    private static final long THRESHOLD = 100000L;

    public ForkJoinSumCalculate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;

        //如果小于临界值，就不拆了
        if (length <= THRESHOLD) {
            long sum = 0L;

            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {

            //获取拆分点
            long middle = (start + end) / 2;
            //拆分左侧
            ForkJoinSumCalculate left = new ForkJoinSumCalculate(start, middle);
            //进行拆分，同时压入线程队列
            left.fork();

            //拆分右侧
            ForkJoinSumCalculate right = new ForkJoinSumCalculate(middle + 1, end);
            //进行拆分，同时压入线程队列
            right.fork();

            //将拆分结果再合并起来
            return left.join() + right.join();
        }
    }
}
