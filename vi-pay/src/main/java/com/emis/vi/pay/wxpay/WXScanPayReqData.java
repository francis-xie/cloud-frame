package com.emis.vi.pay.wxpay;

import com.tencent.common.Configure;
import com.tencent.common.MD5;
import com.tencent.protocol.pay_protocol.ScanPayReqData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-12-2
 * Time: 上午9:10
 * To change this template use File | Settings | File Templates.
 */
public class WXScanPayReqData extends ScanPayReqData {
  /**
   * @param authCode 这个是扫码终端设备从用户手机上扫取到的支付授权号，这个号是跟用户用来支付的银行卡绑定的，有效期是1分钟
   * @param body 要支付的商品的描述信息，用户会在支付成功页面里看到这个信息
   * @param attach 支付订单里面可以填的附加数据，API会将提交的这个附加数据原样返回
   * @param outTradeNo 商户系统内部的订单号,32个字符内可包含字母, 确保在商户系统唯一
   * @param totalFee 订单总金额，单位为“分”，只能整数
   * @param deviceInfo 商户自己定义的扫码支付终端设备号，方便追溯这笔交易发生在哪台终端设备上
   * @param spBillCreateIP 订单生成的机器IP
   * @param timeStart 订单生成时间， 格式为yyyyMMddHHmmss，如2009年12 月25 日9 点10 分10 秒表示为20091225091010。时区为GMT+8 beijing。该时间取自商户服务器
   * @param timeExpire 订单失效时间，格式同上
   * @param goodsTag 商品标记，微信平台配置的商品标记，用于优惠券或者满减使用
   */
  public WXScanPayReqData(String authCode,String body,String attach,String outTradeNo,int totalFee,String deviceInfo,String spBillCreateIP,String timeStart,String timeExpire,String goodsTag){
    super(authCode,body,attach,outTradeNo,totalFee,deviceInfo,spBillCreateIP,timeStart,timeExpire,goodsTag);
    setSign("");//先清空父类的签名
    //根据API给的签名规则进行签名
    String sign =getSign(toMap());
    setSign(sign);//把签名数据设置到Sign这个属性中
  }
  /*覆盖父类获取签名方法*/
  public static String getSign(Map<String,Object> map){
    ArrayList<String> list = new ArrayList<String>();
    for(Map.Entry<String,Object> entry:map.entrySet()){
      if(!"".equals(entry.getValue())){
        list.add(entry.getKey() + "=" + entry.getValue() + "&");
      }
    }
    int size = list.size();
    String [] arrayToSort = list.toArray(new String[size]);
    Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < size; i ++) {
      sb.append(arrayToSort[i]);
    }
    String result = sb.toString();
    result += "key=" + Configure.getKey();
    //Util.log("Sign Before MD5:" + result);
    System.out.println("Sign Before MD5:" + result);
    result = MD5.MD5Encode(result).toUpperCase();
    //Util.log("Sign Result:" + result);
    System.out.println("Sign Result:" + result);
    return result;
  }
  /*覆盖父类方法，获取父类的所有字段*/
  public Map<String,Object> toMap(){
    Map<String,Object> map = new HashMap<String, Object>();
    Class<?> clazz = this.getClass();
    clazz=clazz.getSuperclass();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      Object obj;
      try {
        //设置字段访问权限,私有属性才可以获取
        field.setAccessible(true);
        obj = field.get(this);
        if(obj!=null && !"".equals(field.getName())){
          map.put(field.getName(), obj);
        }
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return map;
  }
}
