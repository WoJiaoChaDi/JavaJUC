package com.atguigu.juc;

/*
* 1.volatile 关键字
* */

public class TestVolatile {

    public static void main(String[] args) {
        ThreadDemo td = new ThreadDemo();
        new Thread(td).start();

        //while (true) 执行效率非常高，main没有机会从主存中再次去获取数据，所以在else里面进行其他操作（或者debugger模式下），就有机会去主存里面获取数据
        while (true) {
            //将td锁住，每次使用td的时候都去刷新一下数据
            synchronized (td){
                if(td.isFlag()){
                    //按理说应该能够打印这行，但实际不行，因为内存可见性的问题，main线程和ThreadDemo是两个线程，
                    System.out.println("------------");
                    break;
                }else{
                    //如果else里面进行操作，则降低了while(true)的效率，让main线程有机会去主存获取数据
                    //System.out.println("+++");
                }
            }
        }
    }

}

class ThreadDemo implements Runnable{

    private boolean flag = false;

    @Override
    public void run() {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        flag = true;

        System.out.println("flag=" + isFlag());
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
