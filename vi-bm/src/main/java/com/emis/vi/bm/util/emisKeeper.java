package com.emis.vi.bm.util;

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
                    emisKeeper = new emisKeeper();
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

    public void reloadEmisPropMap() {
        setEmisPropMap(emisPropUtil.getPropMapfromEntity());
    }

    /**
     * 退出系统前，将值销毁<br>
     * 不能乱调用
     */
    public void close() {
        this.emisPropMap.clear();
    }
}