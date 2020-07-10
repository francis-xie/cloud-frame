package com.emis.vi.bm.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程使用
 */
public class AsyncTaskServiceTest {

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor executor;

    public void asyncExecutorTask() {
        List<Future> futures = new ArrayList<>();
        List<String> userList = new ArrayList<>(Arrays.asList(
                "123@qq.com",
                "456@qq.com"
        ));
        for (String email : userList) {
            Future<?> future = executor.submit(() -> {
                new SendEmailThread(email);
            });
            futures.add(future);
        }
        System.out.println(">>>>>>>>>>>>>>>>end");
    }

    class SendEmailThread implements Callable<String> {
        private String to;

        public SendEmailThread(String to) {
            this.to = to;
        }

        @Override
        public String call() throws Exception {
            System.out.println(">>>>>>>>>>>>>>>>发送邮件,接收邮箱：{}" + to);
            return "接收邮箱：" + to;
        }
    }
}