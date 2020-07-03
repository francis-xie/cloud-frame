package com.emis.vi.pay.jwt.controller;

import com.alibaba.fastjson.JSONObject;
import com.emis.vi.pay.jwt.model.User;
import com.emis.vi.pay.jwt.service.AuthenticationService;
import com.emis.vi.pay.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户登陆controller，
 * 传统的登录验证方式是 session + cookie 的形式，这种验证方式不太适用于只提供 restful api 的后端系统，
 * 所以选择了 基于 token 的验证方式，token 是个长字符串，客户端向服务器申请。
 * 在需要登录的请求中每次都携带上申请到的token，服务器端验证 token 的有效性，只有验证通过了才允许访问
 */
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private AuthenticationService authenticationService;
    private UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    /**
     * 用户登陆 并采用JWT生成token
     *
     * @param user
     * @return
     */
    @PostMapping("")
    public Object login(@RequestBody User user) {
        //1.根据用户名查询该用户是否存在
        User userInDataBase = userService.findByName(user.getName());
        JSONObject jsonObject = new JSONObject();
        if (userInDataBase == null) { //判断用户是否存在
            jsonObject.put("error", "用户不存在");
        } else if (!userService.comparePassword(user, userInDataBase)) {  //比对用户密码是否正确
            jsonObject.put("error", "密码不正确");
        } else {
            //根据用户对象数据生成token 提供给前端保存 后续调用接口认证
            String token = authenticationService.getToken(userInDataBase);
            jsonObject.put("token", token);
            jsonObject.put("user", userInDataBase);
        }
        return jsonObject;
    }
}
