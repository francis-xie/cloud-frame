package com.emis.vi.pay.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.emis.vi.pay.jwt.model.User;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * 用户登陆service
 */
@Service
public class AuthenticationService {
    /**
     * 根据传递的 用户对象 生成 token 返回
     *
     * @param user 用户对象
     * @return
     */
    public String getToken(User user) {
        String token = "";
        try {
            token = JWT.create()
                    .withAudience(user.getId().toString())          // 将 user id 保存到 token 里面
                    .sign(Algorithm.HMAC256(user.getPassword()));   // 以 password 作为 token 的密钥
        } catch (UnsupportedEncodingException ignore) {
        }
        return token;
    }
}
