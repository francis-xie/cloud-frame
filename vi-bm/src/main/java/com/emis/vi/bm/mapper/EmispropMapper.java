package com.emis.vi.bm.mapper;

import com.emis.vi.bm.pojo.Emisprop;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 系统参数mapper，映射匹配对应的EmispropMapper.xml
 * 注意mapper xml文件要和mapper java文件同名，并且包名一致，不同的是xml文件要放在 resource 目录之下
 * <p>
 * 这里使用mapper注解方式查询
 */
@Mapper
@Component
public interface EmispropMapper {

    /**
     * 查询全部系统参数
     *
     * @return
     */
    @Select("select * from emisprop")
    public List<Emisprop> listAll();

    /**
     * 查询指定系统参数
     *
     * @param name 变数名称
     * @return
     */
    @Select("select * from emisprop where NAME = #{name}")
    public Emisprop findByName(String name);

    /**
     * 新增系统参数
     *
     * @param emisprop 系统参数对象
     * @return
     */
    @Insert("insert into emisprop(NAME,VALUE) values(#{name}, #{value})")
    public int add(Emisprop emisprop);

    /**
     * 修改指定系统参数
     *
     * @param emisprop 系统参数对象
     * @return
     */
    @Update("update emisprop set VALUE = #{value} where NAME = #{name}")
    public int upd(Emisprop emisprop);

    /**
     * 删除指定系统参数
     *
     * @param name 变数名称
     * @return
     */
    @Delete("delete from emisprop where NAME = #{name}")
    public int del(String name);
}
