package com.emis.vi.bm.common.sofa.log;

import com.alipay.sofa.common.utils.StringUtil;
import org.slf4j.Logger;
import com.alipay.sofa.common.log.LoggerSpaceManager;

/**
 * 来源：https://github.com/sofastack/sofa-common-tools
 * 可下载github源码参考测试用例：LoggerSpaceManagerUsageTest
 */
public class SofaLoggerFactory {
    private static final String OCR_LOGGER_SPACE = "com.emis.vi.bm";

    public static Logger getLogger(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }
        //从"com/emis/vi/bm/log"中获取vi的日志配置并寻找对应logger对象
        return LoggerSpaceManager.getLoggerBySpace(name, OCR_LOGGER_SPACE);
    }

    public static Logger getLogger(Class<?> klass) {
        if (klass == null) {
            return null;
        }

        return getLogger(klass.getCanonicalName());
    }
}
