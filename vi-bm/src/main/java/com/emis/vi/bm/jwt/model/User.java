package com.emis.vi.bm.jwt.model;

/**
 * 用户表
 */
public class User {
    /*
    注意:这里 id 用 Integer 而不用 int，
    因为 int 自动初始化为0，mybatis mapper 文件 就不能使用 <if test="id!=null"> 了，而 Integer 可以为 null
     */
    private Integer id;
    private String name;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
