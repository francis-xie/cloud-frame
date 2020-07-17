package com.emis.vi.bm.jwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.emis.vi.bm.jwt.annotation.CurrentUser;
import com.emis.vi.bm.jwt.annotation.LoginRequired;
import com.emis.vi.bm.jwt.model.User;
import com.emis.vi.bm.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户Controller，调用service层的方法，返回JSON数据
 */
//用 @RestController 代替了 @Controller，表示该类里面的方法都是返回 JSON 数据，而不用再给每个方法添加@ResponseBody注解
@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 添加用户，{"name": "abc","password": "123456"}
     * @param user
     * @return
     */
    @PostMapping("") //这样对应的路由为POST /api/user
    public Object add(@RequestBody User user) {
        if (userService.findByName(user.getName()) != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("error", "用户名已被使用");
            return jsonObject;
        }
        return userService.add(user);
    }

    @GetMapping("{id}")
    public Object findById(@PathVariable int id) {
        return userService.findById(id);
    }

    //在方法加上自定义的 @LoginRequired 注解，验证是否有登陆
    @LoginRequired
    @GetMapping("/test")
    public Object testLogin() {
        return "success";
    }

    @GetMapping("/test2")
    @LoginRequired
    public Object testCurrentUser(@CurrentUser User user) {
        return user;
    }
}
