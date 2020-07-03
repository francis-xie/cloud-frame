package com.emis.vi.pay.jwt.service;


import com.emis.vi.pay.jwt.mapper.UserMapper;
import com.emis.vi.pay.jwt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用户service业务逻辑处理，调用mapper层的方法
 */
@Service
public class UserService {
    private UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 添加用户
     *
     * @param user 用户对象
     * @return 返回查询到的用户对象数据
     */
    public User add(User user) {
        String passwordHash = passwordToHash(user.getPassword()); //使用 hash 来保存用户密码
        user.setPassword(passwordHash);
        userMapper.add(user);
        return findById(user.getId());
    }

    /**
     * 使用SHA-256哈希算法 对 用户密码进行加密
     *
     * @param password 用户密码
     * @return
     */
    private String passwordToHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(password.getBytes());
            byte[] src = digest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            // 字节数组转16进制字符串
            // https://my.oschina.net/u/347386/blog/182717
            for (byte aSrc : src) {
                String s = Integer.toHexString(aSrc & 0xFF);
                if (s.length() < 2) {
                    stringBuilder.append('0');
                }
                stringBuilder.append(s);
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException ignore) {
        }
        return null;
    }

    public User findById(int id) {
        User user = new User();
        user.setId(id);
        return userMapper.findOne(user);
    }

    public User findByName(String name) {
        User param = new User();
        param.setName(name);
        return userMapper.findOne(param);
    }

    /**
     * 对传递的用户密码加密 和 DB中查询出的用户密码 比对是否一致
     *
     * @param user           传参的用户对象
     * @param userInDataBase DB中查询出的用户对象数据
     * @return
     */
    public boolean comparePassword(User user, User userInDataBase) {
        return passwordToHash(user.getPassword())      // 将用户提交的密码转换为 hash
                .equals(userInDataBase.getPassword()); // 数据库中的 password 已经是 hash，不用转换
    }
}
