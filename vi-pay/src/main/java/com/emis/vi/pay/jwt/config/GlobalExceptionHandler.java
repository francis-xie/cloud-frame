package com.emis.vi.pay.jwt.config;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //@ResponseBody表示这个方法返回 JSON 格式的数据
    @ResponseBody
    //@ExceptionHandler表示该方法可以处理的异常，可以多个，比如
    //@ExceptionHandler({ NullPointerException.class, DataAccessException.class})
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        logger.error(ExceptionUtils.getFullStackTrace(e));  // 记录错误信息
        String msg = e.getMessage();
        if (msg == null || msg.equals("")) {
            msg = "服务器出错";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", msg);
        return jsonObject;
    }
}
