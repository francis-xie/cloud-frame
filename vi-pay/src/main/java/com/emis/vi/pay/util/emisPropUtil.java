package com.emis.vi.pay.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class emisPropUtil {
    private static JdbcTemplate jdbcTemplate;

    //给静态组件加setter方法，并在这个方法上加上@Autowired。Spring能扫描到JdbcTemplate的bean，然后通过setter方法注入
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        emisPropUtil.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 将EMISPROP系统参数资料转成HashMap（所有的资料）
     *
     * @return
     */
    public static HashMap getPropMapfromEntity() {
        HashMap emisPropMap = new HashMap();
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * FROM EMISPROP");
            for (Map<String, Object> map : list) {
                String _sName = emisUtils.parseString(map.get("NAME"));
                String _sValue = emisUtils.parseString(map.get("VALUE"));
                emisPropMap.put(_sName, _sValue);
            }
        } catch (Exception e) {

        } finally {
        }
        return emisPropMap;
    }

    /**
     * 获取EmisProp中的参数
     *
     * @param key
     * @return
     */
    public static Object getEmisProp(String key) {
        if (emisKeeper.getInstance().getEmisPropMap() != null) {
            return emisKeeper.getInstance().getEmisPropMap().get(key);
        }
        return null;
    }

    public static Object getEmisProp(String key, Object def) {
        Object ret = getEmisProp(key);
        return ret == null ? def : ret;
    }

    public static String get(String key) {
        return emisUtils.parseString(getEmisProp(key));
    }

    public static String get(String key, String def) {
        String ret = emisUtils.parseString(getEmisProp(key));
        return ret == null ? def : ret;
    }

    /**
     * 设置EmisProp中的参数
     *
     * @param key
     * @param obj
     */
    @SuppressWarnings("unchecked")
    public static void putEmisProp(String key, Object obj) {
        if (emisKeeper.getInstance().getEmisPropMap() != null) {
            emisKeeper.getInstance().getEmisPropMap().put(key, obj);
        }
    }
}