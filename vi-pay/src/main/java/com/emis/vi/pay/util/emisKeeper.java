package com.emis.vi.pay.util;

import com.emis.vi.pay.util.log4j.LogKit;

import java.util.HashMap;
import java.util.Observable;

/**
 * 单例模式。用来保存<br>
 * 添加的一些对象，记得在close()方法里销毁
 */
public class emisKeeper extends Observable {
    private volatile static emisKeeper emisKeeper = null;

    public static emisKeeper getInstance() {
        if (emisKeeper == null) {
            synchronized (emisKeeper.class) {
                if (emisKeeper == null) {
                    LogKit.error("emisKeeper is null===");
                    emisKeeper = new emisKeeper();
                    LogKit.error(emisUtils.getSystemInfo());
                }
            }
        }
        return emisKeeper;
    }

    /**
     * 系统参数(放最开始)
     */
    private HashMap emisPropMap = new HashMap();

    /**
     * @param emisPropMap the emisPropMap to set
     */
    public void setEmisPropMap(HashMap emisPropMap) {
        if (this.emisPropMap != null) {
            this.emisPropMap.clear();
            //this.emisPropMap = null;//不能给null,会导致其他地方值丢失，指针地址改变了。
            this.emisPropMap.putAll(emisPropMap);
        } else {
            this.emisPropMap = emisPropMap;
        }
    }

    /**
     * @return the emisPropMap
     */
    public HashMap getEmisPropMap() {
        if (emisPropMap == null || emisPropMap.isEmpty()) {
            emisPropMap = emisPropUtil.getPropMapfromEntity();
        }
        return emisPropMap;
    }

    /**
     * 退出系统前，将值销毁<br>
     * 不能乱调用
     */
    public void close() {
        this.emisPropMap.clear();
    }
}