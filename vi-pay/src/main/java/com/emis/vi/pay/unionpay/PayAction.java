package com.emis.vi.pay.unionpay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.emis.vi.pay.unionpay.util.AcpService;
import com.emis.vi.pay.unionpay.util.SDKConfig;
import com.emis.vi.pay.unionpay.util.SDKConstants;

/**
 * 银联二维码支付对接
 * 2017/12/15 Francis.xie Modify 需求 #41562 [专案-爱维尔]对接银联扫码支付-后台调整
 */
public class PayAction {

  /**
   * 返回报文
   */
  private String responseBody = "";

  //默认配置的是UTF-8
  public static String encoding = "UTF-8";

  //全渠道固定值
  public static String version = SDKConfig.getConfig().getVersion();

  //后台服务对应的写法参照 BackRcvResponse.java
  public static String backUrl = SDKConfig.getConfig().getBackUrl();//受理方和发卡方自选填写的域[O]--后台通知地址

  // 商户发送交易时间 格式:YYYYMMDDhhmmss
  public static String getCurrentTime() {
    return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
  }

  // AN8..40 商户订单号，不能含"-"或"_"
  public static String getOrderId() {
    return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
  }

  /**
   * 二维码支付产品接口规范2.3.pdf
   * 5.2 被扫类 5.2.1 二维码消费
   * 5.2.1.1 概述
   * 持卡人被商户用扫描枪扫码，发起支付。银联收到消费请求后将立刻返回同步应答，然后继续处理
   * 消费交易，当消费交处理完毕后再通过消费结果通知将交易结果返回给商户系统。
   *
   * @param merId   商户代码
   * @param qrNo    C2B 码
   * @param orderId 商户订单号
   * @param txnTime 订单发送时间
   * @param txnAmt  交易金额 单位为分
   * @param termId  终端号
   */
  public void consume(String merId, String qrNo, String orderId, String txnTime, String txnAmt, String termId) {
    try {
      Map<String, String> contentData = new HashMap<String, String>();
      /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
      contentData.put("version", version); //版本号 固定填写全渠道默认值5.1.0
      contentData.put("encoding", encoding); //编码方式 填写报文使用的字符编码UTF-8|GBK|GB2312|GB18030 若不填写，默认取值： UTF-8
      contentData.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法 01（表示采用 RSA 签名）
      contentData.put("txnType", "01"); //交易类型 01：消费
      contentData.put("txnSubType", "06"); //交易子类 06：二维码消费
      contentData.put("bizType", "000000"); //产品类型 默认取值:000000
      contentData.put("channelType", "07"); //渠道类型 08：移动 07：互联网

      //后台通知地址（需设置为外网能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，【支付失败的交易银联不会发送后台通知】
      //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
      //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
      //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200或302，那么银联会间隔一段时间再次发送。总共发送5次，银联后续间隔1、2、4、5 分钟后会再次通知。
      //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
      contentData.put("backUrl", backUrl);

      /***商户接入参数***/
      contentData.put("accessType", "0"); //接入类型，0：普通商户直连接入 ，不需修改（0：直连商户， 1： 收单机构 2：平台商户）
      contentData.put("merId", merId); //商户代码，请改成自己申请的商户号或者open上注册得来的777商户号测试
      contentData.put("qrNo", qrNo); //C2B码,1-20位数字
      contentData.put("orderId", orderId); //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
      contentData.put("txnTime", txnTime); //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
      contentData.put("txnAmt", txnAmt); //交易金额 单位为分，不能带小数点
      contentData.put("currencyCode", "156"); //境内商户固定 156 人民币
      contentData.put("termId", termId); //终端号

      // 请求方保留域，
      // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
      // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
      // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
      // contentData.put("reqReserved", "透传信息1|透传信息2|透传信息3");
      // 2. 内容可能出现&={}[]"'符号时：
      // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
      // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
      //    注意控制数据长度，实际传输的数据长度不能超过1024位。
      //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
      // contentData.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

      /**对请求参数进行签名并发送http post请求，接收同步应答报文**/
      Map<String, String> reqData = AcpService.sign(contentData, encoding); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
      //String reqMessage = genHtmlResult(reqData);
      //SDKConfig.getConfig().getLogger().info("请求报文:<br/>" + reqMessage);

      String requestAppUrl = SDKConfig.getConfig().getBackRequestUrl(); //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
      //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
      Map<String, String> rspData = AcpService.post(reqData, requestAppUrl, encoding);
      //String rspMessage = genHtmlResult(rspData);
      //SDKConfig.getConfig().getLogger().info("应答报文:</br>" + rspMessage);

      /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
      //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
      if (!rspData.isEmpty()) {
        if (AcpService.validate(rspData, encoding)) {
          //SDKConfig.getConfig().getLogger().info("验证签名成功");
          setResponseMsg("1", rspData.get("respCode"), rspData.get("respMsg"), "", "", rspData.get("orderId"), rspData.get("txnTime"),
            rspData.get("queryId"), "", "", "", rspData.get("txnAmt"), "", "", "");
        } else {
          setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"验证签名失败!\"}");
          //SDKConfig.getConfig().getLogger().error("验证签名失败");
          //TODO 检查验证签名失败的原因
        }
      } else {
        //未返回正确的http状态
        setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"未获取到返回报文或返回http状态码非200!\"}");
        SDKConfig.getConfig().getLogger().error("未获取到返回报文或返回http状态码非200");
      }
    } catch (Exception e) {
      setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"后台处理异常,请联系管理人员!\"}");
      SDKConfig.getConfig().getLogger().error(e, e);
    }
  }

  /**
   * 二维码支付产品接口规范2.3.pdf
   * 5.4.1 消费撤销
   * 5.4.1.1 概述
   * 撤销必须与原始消费在同一天（准确讲是前日23:00至本日23:00之间），且撤销必须是全金额撤销。
   *
   * @param merId       商户代码
   * @param orderId     商户订单号
   * @param txnTime     订单发送时间
   * @param origQryId   原始交易流水号
   * @param origOrderId 原交易商户订单号
   * @param origTxnTime 原交易商户发送交易时间
   * @param txnAmt      交易金额 与原始消费交易一致
   * @param termId      终端号
   */
  public void consumeUndo(String merId, String orderId, String txnTime, String origQryId, String origOrderId, String origTxnTime, String txnAmt, String termId) {
    try {
      Map<String, String> data = new HashMap<String, String>();
      /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
      data.put("version", version); //版本号
      data.put("encoding", encoding); //编码方式 默认取值： UTF-8
      data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法 01（表示采用 RSA 签名）
      data.put("txnType", "31"); //交易类型 取值： 31：消费撤销
      data.put("txnSubType", "00"); //交易子类 默认： 00
      data.put("bizType", "000000"); //产品类型
      data.put("channelType", "07"); //渠道类型 08：移动 07：互联网
      data.put("backUrl", backUrl); //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费撤销交易 商户通知,其他说明同消费交易的商户通知

      /***商户接入参数***/
      data.put("accessType", "0"); //接入类型，商户接入固定填0，不需修改
      data.put("merId", merId); //商户代码，请改成自己申请的商户号或者open上注册得来的777商户号测试
      data.put("orderId", orderId); //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
      data.put("txnTime", txnTime); //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
      /***要调通交易以下字段必须修改 2.origQryId 、 origOrderId + origTxnTime、 二者必送其一***/
      if (!"".equals(origQryId))
        data.put("origQryId", origQryId); //原始消费交易的 queryId
      if (!"".equals(origOrderId))
        data.put("origOrderId", origOrderId); //原始消费交易的 orderId
      if (!"".equals(origTxnTime))
        data.put("origTxnTime", origTxnTime); //原始消费交易的 txnTime
      data.put("txnAmt", txnAmt); //【撤销金额】，消费撤销时必须和原消费金额相同
      data.put("termId", termId); //终端号

      // 请求方保留域，
      // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
      // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
      // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
      // data.put("reqReserved", "透传信息1|透传信息2|透传信息3");
      // 2. 内容可能出现&={}[]"'符号时：
      // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
      // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
      //    注意控制数据长度，实际传输的数据长度不能超过1024位。
      //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
      // data.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

      /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文**/
      Map<String, String> reqData = AcpService.sign(data, encoding); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
      //String reqMessage = genHtmlResult(reqData);
      //SDKConfig.getConfig().getLogger().info("请求报文:<br/>" + reqMessage);

      String url = SDKConfig.getConfig().getBackRequestUrl(); //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
      //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
      Map<String, String> rspData = AcpService.post(reqData, url, encoding);
      //String rspMessage = genHtmlResult(rspData);
      //SDKConfig.getConfig().getLogger().info("应答报文:</br>" + rspMessage);

      //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
      if (!rspData.isEmpty()) {
        if (AcpService.validate(rspData, encoding)) {
          //SDKConfig.getConfig().getLogger().info("验证签名成功");
          setResponseMsg("3", rspData.get("respCode"), rspData.get("respMsg"), "", "", rspData.get("orderId"), rspData.get("txnTime"),
            rspData.get("queryId"), rspData.get("origOrderId"), rspData.get("origTxnTime"), "", rspData.get("txnAmt"), "", "", "");
        } else {
          setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"验证签名失败!\"}");
          //SDKConfig.getConfig().getLogger().error("验证签名失败");
          //TODO 检查验证签名失败的原因
        }
      } else {
        //未返回正确的http状态
        setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"未获取到返回报文或返回http状态码非200!\"}");
        SDKConfig.getConfig().getLogger().error("未获取到返回报文或返回http状态码非200");
      }
    } catch (Exception e) {
      setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"后台处理异常,请联系管理人员!\"}");
      SDKConfig.getConfig().getLogger().error(e, e);
    }
  }

  /**
   * 二维码支付产品接口规范2.3.pdf
   * 5.4.2 退货
   * 5.4.2.1 概述
   * 退货可以在原始消费发生后30日内，可以部分金额退货，可以多次退货，但累计退货金额不能超过原始消费金额。
   *
   * @param merId       商户代码
   * @param orderId     商户订单号
   * @param txnTime     订单发送时间
   * @param origQryId   原始交易流水号
   * @param origOrderId 原交易商户订单号
   * @param origTxnTime 原交易商户发送交易时间
   * @param txnAmt      交易金额
   * @param termId      终端号
   */
  public void refund(String merId, String orderId, String txnTime, String origQryId, String origOrderId, String origTxnTime, String txnAmt, String termId) {
    try {
      Map<String, String> data = new HashMap<String, String>();
      /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
      data.put("version", version); //版本号 固定填写
      data.put("encoding", encoding); //编码方式 默认取值： UTF-8
      data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法 01（表示采用 RSA 签名）
      data.put("txnType", "04"); //交易类型 取值： 04：退货
      data.put("txnSubType", "00"); //交易子类 默认： 00
      data.put("bizType", "000000"); //产品类型
      data.put("channelType", "07"); //渠道类型 08：移动 07：互联网
      data.put("backUrl", backUrl); //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知

      /***商户接入参数***/
      data.put("accessType", "0"); //接入类型，商户接入固定填0，不需修改
      data.put("merId", merId); //商户代码，请改成自己申请的商户号或者open上注册得来的777商户号测试
      data.put("orderId", orderId); //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
      data.put("txnTime", txnTime); //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
      /***要调通交易以下字段必须修改 2.origQryId 、 origOrderId + origTxnTime、 二者必送其一***/
      if (!"".equals(origQryId))
        data.put("origQryId", origQryId); //原始消费交易的 queryId；
      if (!"".equals(origOrderId))
        data.put("origOrderId", origOrderId); //原始消费交易的 orderId；
      if (!"".equals(origTxnTime))
        data.put("origTxnTime", origTxnTime); //原始消费交易的 txnTime；
      data.put("txnAmt", txnAmt); //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
      data.put("termId", termId); //****终端号

      // 请求方保留域，
      // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
      // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
      // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
      // data.put("reqReserved", "透传信息1|透传信息2|透传信息3");
      // 2. 内容可能出现&={}[]"'符号时：
      // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
      // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
      //    注意控制数据长度，实际传输的数据长度不能超过1024位。
      //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
      // data.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));

      /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
      Map<String, String> reqData = AcpService.sign(data, encoding); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
      //String reqMessage = genHtmlResult(reqData);
      //SDKConfig.getConfig().getLogger().info("请求报文:<br/>" + reqMessage);

      String url = SDKConfig.getConfig().getBackRequestUrl(); //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
      //这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
      Map<String, String> rspData = AcpService.post(reqData, url, encoding);
      //String rspMessage = genHtmlResult(rspData);
      //SDKConfig.getConfig().getLogger().info("应答报文:</br>" + rspMessage);

      /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
      //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
      if (!rspData.isEmpty()) {
        if (AcpService.validate(rspData, encoding)) {
          //SDKConfig.getConfig().getLogger().info("验证签名成功");
          setResponseMsg("4", rspData.get("respCode"), rspData.get("respMsg"), "", "", rspData.get("orderId"), rspData.get("txnTime"),
            rspData.get("queryId"), rspData.get("origOrderId"), rspData.get("origTxnTime"), "", rspData.get("txnAmt"), "", "", "");
        } else {
          setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"验证签名失败!\"}");
          //SDKConfig.getConfig().getLogger().error("验证签名失败");
          //TODO 检查验证签名失败的原因
        }
      } else {
        //未返回正确的http状态
        setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"未获取到返回报文或返回http状态码非200!\"}");
        SDKConfig.getConfig().getLogger().error("未获取到返回报文或返回http状态码非200");
      }
    } catch (Exception e) {
      setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"后台处理异常,请联系管理人员!\"}");
      SDKConfig.getConfig().getLogger().error(e, e);
    }
  }

  /**
   * 二维码支付产品接口规范2.3.pdf
   * 5.5.1 交易状态查询
   * 5.5.1.1 概述
   * 对于未收到交易结果的联机交易，商户必须向银联全渠道支付平台发起交易状态查询交易，查询交
   * 易结果。完成交易的过程不需要同持卡人交互，属于后台交易。交易查询类交易可由商户通过SDK向银
   * 联全渠道支付交易平台发起交易
   * <p/>
   * 后台类交易查询机制：
   * 后台类交易：如代收，代付，后台消费，退货，消费撤销；对于交易状态未知的交易请求方必须发起交易状态查询交易。
   * 后台类资金类交易同步返回00 银联已受理，交易完成银联有后台通知，商户也可以发起 查询交
   * 易，可查询N次（不超过6次），每次时间间隔2的N次秒发起,即间隔1、 2、 4、 8、 16、 32秒查询；
   * 查询5次以上，仍获取不到明确状态的交易，后续可以间隔更长时间发起查询，最终结果以对账文件为准。
   *
   * @param merId   商户代码
   * @param queryId 交易查询流水号
   * @param orderId 商户订单号
   * @param txnTime 订单发送时间
   */
  public void query(String merId, String queryId, String orderId, String txnTime) {
    try {
      Map<String, String> data = new HashMap<String, String>();
      /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
      data.put("version", version); //版本号 固定填写
      data.put("encoding", encoding); //编码方式 默认取值： UTF-8
      data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法 01（表示采用 RSA 签名）
      data.put("txnType", "00"); //交易类型 00 查询交易
      data.put("txnSubType", "00"); //交易子类 默认： 00
      data.put("bizType", "000000"); //产品类型 默认:000000
      //data.put("channelType", "07"); //渠道类型 08：移动 07：互联网

      /***商户接入参数***/
      data.put("accessType", "0"); //接入类型，商户接入固定填0，不需修改
      data.put("merId", merId); //商户代码，请改成自己申请的商户号或者open上注册得来的777商户号测试
      /***要调通交易以下字段必须修改 同被查询交易orderId + txnTime 、 queryId二者必送其一***/
      if (!"".equals(queryId))
        data.put("queryId", queryId); //交易查询流水号 交易子类为 02 流水号查询时必填
      data.put("orderId", orderId); //商户订单号 同被查询交易交易子类为 00 时必填
      data.put("txnTime", txnTime); //订单发送时间 同被查询交易交易子类为 00 时必填

      /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
      Map<String, String> reqData = AcpService.sign(data, encoding); //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
      //String reqMessage = genHtmlResult(reqData);
      //SDKConfig.getConfig().getLogger().info("请求报文:<br/>" + reqMessage);

      String url = SDKConfig.getConfig().getSingleQueryUrl(); //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
      //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
      Map<String, String> rspData = AcpService.post(reqData, url, encoding);
      //String rspMessage = genHtmlResult(rspData);
      //SDKConfig.getConfig().getLogger().info("应答报文:</br>" + rspMessage);

      /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
      //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
      if (!rspData.isEmpty()) {
        if (AcpService.validate(rspData, encoding)) {
          //SDKConfig.getConfig().getLogger().info("验证签名成功");
          setResponseMsg("2", rspData.get("respCode"), rspData.get("respMsg"), rspData.get("origRespCode"), rspData.get("origRespMsg"), rspData.get("orderId"), rspData.get("txnTime"),
            rspData.get("queryId"), rspData.get("origOrderId"), rspData.get("origTxnTime"), rspData.get("settleAmt"), rspData.get("txnAmt"),
            rspData.get("accNo"), rspData.get("payCardType"), rspData.get("payType"));
        } else {
          setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"验证签名失败!\"}");
          //SDKConfig.getConfig().getLogger().error("验证签名失败");
          //TODO 检查验证签名失败的原因
        }
      } else {
        //未返回正确的http状态
        setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"未获取到返回报文或返回http状态码非200!\"}");
        SDKConfig.getConfig().getLogger().error("未获取到返回报文或返回http状态码非200");
      }
    } catch (Exception e) {
      setResponseBody("{\"respCode\":\"ZZZZ\",\"respMsg\":\"后台处理异常,请联系管理人员!\"}");
      SDKConfig.getConfig().getLogger().error(e, e);
    }
  }

  /**
   * 组装请求，返回报文字符串用于显示
   *
   * @param data
   * @return
   */
  public static String genHtmlResult(Map<String, String> data) {
    TreeMap<String, String> tree = new TreeMap<String, String>();
    Iterator<Entry<String, String>> it = data.entrySet().iterator();
    while (it.hasNext()) {
      Entry<String, String> en = it.next();
      tree.put(en.getKey(), en.getValue());
    }
    it = tree.entrySet().iterator();
    StringBuffer sf = new StringBuffer();
    while (it.hasNext()) {
      Entry<String, String> en = it.next();
      String key = en.getKey();
      String value = en.getValue();
      if ("respCode".equals(key)) {
        sf.append("<b>" + key + SDKConstants.EQUAL + value + "</br></b>");
      } else
        sf.append(key + SDKConstants.EQUAL + value + "</br>");
    }
    return sf.toString();
  }

  /**
   * 返回前台信息
   *
   * @param service
   * @param respCode     应答码
   * @param respMsg      应答信息
   * @param origRespCode 原交易应答码
   * @param origRespMsg  原交易应答信息
   * @param orderId      商户订单号 被查询交易的订单号
   * @param txnTime      订单发送时间 被查询交易的交易时间
   * @param queryId      交易查询流水号 被查询交易查询流水号
   * @param origOrderId  原交易商户订单号 查询交易为退货或者消费撤销时返回，表示原消费交易的商户订单号
   * @param origTxnTime  原交易商户发送交易时间 查询交易为退货或者消费撤销时返回，表示原消费交易的商户发送交易时间
   * @param settleAmt    清算金额
   * @param txnAmt       交易金额
   * @param accNo        账号
   * @param payCardType  支付卡类型
   * @param payType      支付方式
   */
  public void setResponseMsg(String service, String respCode, String respMsg, String origRespCode, String origRespMsg,
                             String orderId, String txnTime, String queryId, String origOrderId, String origTxnTime, String settleAmt, String txnAmt,
                             String accNo, String payCardType, String payType) {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"service\":\"" + service + "\",");
    sb.append("\"respCode\":\"" + respCode + "\",");
    sb.append("\"respMsg\":\"" + respMsg + "\",");
    sb.append("\"origRespCode\":\"" + origRespCode + "\",");
    sb.append("\"origRespMsg\":\"" + origRespMsg + "\",");
    sb.append("\"orderId\":\"" + orderId + "\",");
    sb.append("\"txnTime\":\"" + txnTime + "\",");
    sb.append("\"queryId\":\"" + queryId + "\",");
    sb.append("\"origOrderId\":\"" + origOrderId + "\",");
    sb.append("\"origTxnTime\":\"" + origTxnTime + "\",");
    sb.append("\"settleAmt\":\"" + settleAmt + "\",");
    sb.append("\"txnAmt\":\"" + txnAmt + "\",");
    sb.append("\"accNo\":\"" + accNo + "\",");
    sb.append("\"payCardType\":\"" + payCardType + "\",");
    sb.append("\"payType\":\"" + payType + "\"");
    sb.append("}");

    setResponseBody(sb.toString());
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String responseBody) {
    this.responseBody = responseBody;
  }

}