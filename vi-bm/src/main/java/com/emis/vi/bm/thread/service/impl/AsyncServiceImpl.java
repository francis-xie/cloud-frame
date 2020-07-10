package com.emis.vi.bm.thread.service.impl;

import com.emis.vi.bm.thread.service.AsyncService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 创建service层的接口的实现
 */
@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    //将Service层的服务异步化，将该service层做的事情提交到线程池中去处理
    //@Async("asyncServiceExecutor") 注解表明executeAsync方法进入的线程池是asyncServiceExecutor方法创建
    @Async("asyncServiceExecutor") //表明executeAsync方法进入的线程池是asyncServiceExecutor方法创建的
    public void executeAsync() {
        System.out.println("start executeAsync");
        try {
            Thread.sleep(1000); //sleep一秒钟
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end executeAsync");
    }
}