package com.emis.vi.bm.thread.hutool;

public class TestThread {
    public static void main(String[] args) {
        System.out.println("execute start");
        ThreadUtil.execute(new RunnableDemo1());
        ThreadUtil.execute(new RunnableDemo2());
        ThreadUtil.execute(new RunnableDemo3());
        System.out.println("execute end");
    }
}

class RunnableDemo1 implements Runnable {

    @Override
    public void run() {
        System.out.println("RunnableDemo1");
    }
}

class RunnableDemo2 implements Runnable {

    @Override
    public void run() {
        System.out.println("RunnableDemo2");
    }
}

class RunnableDemo3 implements Runnable {

    @Override
    public void run() {
        System.out.println("RunnableDemo3");
    }
}