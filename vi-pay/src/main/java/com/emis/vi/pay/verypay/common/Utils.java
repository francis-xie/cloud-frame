package com.emis.vi.pay.verypay.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * $Id$
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class Utils {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");

  public static String getDate(long time) {
    return new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date(time));
  }

  public static String getOutSN() {
    // 年月日时分秒+微妙数的后5未+6位随机数
    // 6位的随机需要注意左边补零 比如 随机出99 补齐6位 000099
    return getRandomFileName();
  }

  private static String getRandomFileName() {
    String timeStr = sdf.format(new Date());
    int random = (int) ((Math.random() * 9 + 1) * 100000000);
    String result = timeStr + String.format("%09d", random);
    return result;
  }

  public static Map<String, Object> MapValueEncoder(Map<String, Object> params2) throws UnsupportedEncodingException {
    Object[] key_arr = params2.keySet().toArray();
    Arrays.sort(key_arr);

    for (Object key : key_arr) {
      if (key.equals("sign"))
        continue;
      Object val = params2.get(key);
      //System.out.println(key + "=" + val);
      val = URLEncoder.encode(val.toString(), "UTF-8");
      params2.put(key.toString(), val);
    }
    return params2;
  }
}