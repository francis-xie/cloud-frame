package com.emis.vi.bm.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorApi {

    public static void main(String[] args) {

    }

    /**
     * 线程池之ThreadPoolExecutor使用
     */
    public static void testThreadPoolExecutor() {
        /*  一.ThreadPoolExecutor参数解释：
            1.corePoolSize 核心线程池大小、2.maximumPoolSize 最大线程池大小，
              线程池执行器将会根据corePoolSize和maximumPoolSize自动地调整线程池大小
              当在execute(Runnable)方法中提交新任务并且少于corePoolSize线程正在运行时，
              即使其他工作线程处于空闲状态，也会创建一个新线程来处理该请求。
              如果有多于corePoolSize但小于maximumPoolSize线程正在运行，则仅当队列已满时才会创建新线程
            3.keepAliveTime 线程最大空闲时间、4.TimeUnit 时间单位，
              如果线程池当前拥有超过corePoolSize的线程，那么多余的线程在空闲时间超过keepAliveTime时会被终止。
              这提供了一种在不积极使用线程池时减少资源消耗的方法。默认情况下，keep-alive策略仅适用于存在超过corePoolSize线程的情况
            5.workQueue 线程等待队列，如果当前线程池任务线程数量小于核心线程池数量，执行器总是优先创建一个任务线程，而不是从线程队列中取一个空闲线程。
              如果当前线程池任务线程数量大于核心线程池数量，执行器总是优先从线程队列中取一个空闲线程，而不是创建一个任务线程。
              如果当前线程池任务线程数量大于核心线程池数量，且队列中无空闲任务线程，将会创建一个任务线程，
              直到超出maximumPoolSize，如果超时maximumPoolSize，则任务将会被拒绝。
            6.threadFactory 线程创建工厂，新线程使用ThreadFactory创建。 如果未另行指定，则使用Executors.defaultThreadFactory默认工厂，
              使其全部位于同一个ThreadGroup中，并且具有相同的NORM_PRIORITY优先级和非守护进程状态
            7.handler 拒绝策略，拒绝任务有两种情况：1. 线程池已经被关闭；2. 任务队列已满且maximumPoolSizes已满；
         */
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 4,
                60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
