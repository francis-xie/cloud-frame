package com.emis.vi.pay.hzsmk.util;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class ConstantUtil {
	
	public static String coverMap2String(Map<String, Object> data) {
        TreeMap<String, Object> tree = new TreeMap();
        Iterator<Map.Entry<String, Object>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> en = (Map.Entry) it.next();

            if (StringUtils.isNotEmpty((String)en.getValue())) {
                if (!"signAture".equals(((String) en.getKey()).trim())) {

                    tree.put((String) en.getKey(), en.getValue());
                }
            } else {
                System.out.println("报文签名/验签准备中：[ 移除空 value对应的key(" + (String) en.getKey() + ") ]");
            }
        }
        it = tree.entrySet().iterator();
        StringBuffer sf = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<String, Object> en = (Map.Entry) it.next();
            sf.append((String) en.getKey() + "=" + en.getValue() + "&");
        }
        return sf.substring(0, sf.length() - 1);
    }

}
