package com.emis.vi.pay.wxpay.V2;

//import com.emis.db.emisDb;
import com.emis.vi.pay.wxpay.V2.bean.emisWXPayStoreSettingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * $Id$
 * 需求 #40662 [标准版] 微信支付增加支持多收款账户-核心程式调整
 * 设置bean, 和作业Z042及table(PAY_SETTING_H) PS_TYPE = 'wxpay' 对应
 */
public class emisWXPayStoreSettingKeeper extends Observable {

  private volatile static emisWXPayStoreSettingKeeper emisWXPayStoreSettingKeeper = null;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  public static emisWXPayStoreSettingKeeper getInstance() {
    if (emisWXPayStoreSettingKeeper == null) {
      synchronized (emisWXPayStoreSettingKeeper.class) {
        if (emisWXPayStoreSettingKeeper == null) {
          emisWXPayStoreSettingKeeper = new emisWXPayStoreSettingKeeper();
        }
      }
    }
    return emisWXPayStoreSettingKeeper;
  }

  /**
   * 设置map (S_NO, 设置bean)
   */
  private HashMap<String, emisWXPayStoreSettingBean> WXPaySetting = new HashMap<String, emisWXPayStoreSettingBean>();

  /**
   * 获取门店微信支付参数
   *
   * @param context_
   * @param S_NO     门店编号
   * @return
   */
  public emisWXPayStoreSettingBean getStoreWXPaySetting(ServletContext context_, String S_NO) {
    if (S_NO == null || "".equals(S_NO.trim())) return null;
    if (WXPaySetting == null) {
      WXPaySetting = new HashMap<String, emisWXPayStoreSettingBean>();
    }
    if (WXPaySetting.size() <= 0) {
      initWXPaySetting(context_);
    }

    if (WXPaySetting.get(S_NO) == null) {
      return WXPaySetting.get("");
    } else {
      return WXPaySetting.get(S_NO);
    }
  }

  private void initWXPaySetting(ServletContext context_) {
//    emisDb oDataSrc_ = null;
    try {
//      oDataSrc_ = emisDb.getInstance(context_);
      StringBuffer selPaySetting = new StringBuffer();
      selPaySetting.append(" select h.PS_TYPE, h.PS_NO, h.PS_PRIMARY, h.WXPAY_MODE, h.WXPAY_APPID, h.WXPAY_MCHID, h.WXPAY_SUBAPPID, h.WXPAY_SUBMCHID, h.WXPAY_KEY\n ");
      selPaySetting.append(" , h.WXPAY_CERTLOCALPATH, h.WXPAY_CERTPASSWORD, isnull(s.S_NO,'') as S_NO\n ");
      selPaySetting.append(" from Pay_Setting_H h\n ");
      selPaySetting.append(" left join Pay_Setting_S s on s.PS_TYPE = h.PS_TYPE and s.PS_NO = h.PS_NO\n ");
      selPaySetting.append(" where h.PS_TYPE = 'wxpay' and (h.PS_PRIMARY = 'Y' or (h.PS_PRIMARY = 'N' and isnull(s.S_NO,'') != '')) \n ");
//      oDataSrc_.prepareStmt(selPaySetting.toString());
//      oDataSrc_.prepareQuery();
      List<Map<String, Object>> list =  jdbcTemplate.queryForList(selPaySetting.toString());
      WXPaySetting.clear();
//      while (oDataSrc_.next()) {
      for (Map<String, Object> map : list) {
        emisWXPayStoreSettingBean storeSetting = new emisWXPayStoreSettingBean();
        storeSetting.setPS_NO(map.get("PS_NO").toString());
        storeSetting.setPS_PRIMARY(map.get("PS_PRIMARY").toString());
        storeSetting.setWXPAY_MODE(map.get("WXPAY_MODE").toString());
        storeSetting.setWXPAY_APPID(map.get("WXPAY_APPID").toString());
        storeSetting.setWXPAY_MCHID(map.get("WXPAY_MCHID").toString());
        storeSetting.setWXPAY_SUBAPPID(map.get("WXPAY_SUBAPPID").toString());
        storeSetting.setWXPAY_SUBMCHID(map.get("WXPAY_SUBMCHID").toString());
        storeSetting.setWXPAY_KEY(map.get("WXPAY_KEY").toString());
        storeSetting.setWXPAY_CERTLOCALPATH(map.get("WXPAY_CERTLOCALPATH").toString());
        storeSetting.setWXPAY_CERTPASSWORD(map.get("WXPAY_CERTPASSWORD").toString());
        WXPaySetting.put(map.get("S_NO").toString(), storeSetting);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
//      if (oDataSrc_ != null) {
//        oDataSrc_.close();
//        oDataSrc_ = null;
//      }
    }
  }

  public void reloadWXPaySetting(ServletContext context_) {
    initWXPaySetting(context_);
  }

}