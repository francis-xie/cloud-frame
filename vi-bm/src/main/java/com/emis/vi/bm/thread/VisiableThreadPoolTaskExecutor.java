package com.emis.vi.bm.thread;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 扩展ThreadPoolTaskExecutor，在每次提交线程的时候将当前线程池的运行状况打印出来
 * Override了父类的execute、submit等方法，在里面调用showThreadPoolInfo方法，
 * 这样每次有任务被提交到线程池的时候，都会将当前线程池的基本情况打印到日志中
 */
public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private void showThreadPoolInfo(String prefix) {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

        if (null == threadPoolExecutor) {
            return;
        }

        //将任务总数、已完成数、活跃线程数，队列大小都打印出来
        System.out.println("{}, {},taskCount [{}], completedTaskCount [{}], activeCount [{}], queueSize [{}]" +
                this.getThreadNamePrefix() +
                prefix +
                threadPoolExecutor.getTaskCount() +
                threadPoolExecutor.getCompletedTaskCount() +
                threadPoolExecutor.getActiveCount() +
                threadPoolExecutor.getQueue().size());
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo("1. do execute");
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo("2. do execute");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo("1. do submit");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo("2. do submit");
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        showThreadPoolInfo("1. do submitListenable");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        showThreadPoolInfo("2. do submitListenable");
        return super.submitListenable(task);
    }
}