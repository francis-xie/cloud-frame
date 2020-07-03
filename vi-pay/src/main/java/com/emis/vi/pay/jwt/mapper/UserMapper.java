package com.emis.vi.pay.jwt.mapper;

import com.emis.vi.pay.jwt.model.User;
import org.springframework.stereotype.Component;

/**
 * 用户mapper，映射匹配对应的UserMapper.xml
 * 注意mapper xml文件要和mapper java文件同名，并且包名一致，不同的是xml文件要放在 resource 目录之下
 */
@Component
public interface UserMapper {
    int add(User user);

    User findOne(User user);
}
