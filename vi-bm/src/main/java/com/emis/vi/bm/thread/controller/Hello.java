package com.emis.vi.bm.thread.controller;

import com.emis.vi.bm.thread.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建controller，定义一个http接口，调用service层的服务
 */
@RestController
public class Hello {

    @Autowired
    private AsyncService asyncService;

    @RequestMapping("/")
    public String submit() {
        System.out.println("start submit");

        //调用service层的任务
        asyncService.executeAsync();

        System.out.println("end submit");

        return "success";
    }
}