package com.emis.vi.bm.thread.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 公共线程池配置类
 */
@Configuration
@EnableAsync
public class ThreadAsyncConfigurer implements AsyncConfigurer {

    @Value("${spring.task.execution.thread-name-prefix:thread_name_prefix_}")
    private String threadNamePrefix;
    @Value("${spring.task.scheduling.pool.size:10}")
    private int corePoolSize;
    @Value("${spring.task.execution.pool.max-size:100}")
    private int maxPoolSize;
    @Value("${spring.task.execution.pool.queue-capacity:10}")
    private int queueCapacity;
    @Value("${spring.task.execution.pool.keep-alive:60}")
    private int keepAliveSeconds;
    private int awaitTerminationMillis = 60;
    private boolean waitForJobsToCompleteOnShutdown = Boolean.TRUE;

    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //设置核心线程数
        threadPool.setCorePoolSize(corePoolSize);
        //设置最大线程数
        threadPool.setMaxPoolSize(maxPoolSize);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(queueCapacity);
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(waitForJobsToCompleteOnShutdown);
        //等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(awaitTerminationMillis);
        //线程空闲后的最大存活时间
        threadPool.setKeepAliveSeconds(keepAliveSeconds);
        //线程名称前缀
        threadPool.setThreadNamePrefix(threadNamePrefix);
        //rejection-policy：当pool已经达到max size的时候，如何处理新任务
        //CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPool.setThreadNamePrefix(threadNamePrefix);//  线程名称前缀
        //初始化线程
        threadPool.initialize();
        return threadPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        //return new SimpleAsyncUncaughtExceptionHandler();
        return new AsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     *
     * @author liunh
     */
    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
            System.out.println("SysAsyncExecutor Exception message - {}" + throwable.getMessage());
            System.out.println("SysAsyncExecutor Exception Method name - {}" + method.getName());
            for (Object param : obj) {
                System.out.println("SysAsyncExecutor Exception Parameter value - " + param);
            }
        }
    }
}