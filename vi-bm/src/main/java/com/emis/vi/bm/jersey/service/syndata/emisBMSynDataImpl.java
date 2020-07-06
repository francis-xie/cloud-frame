package com.emis.vi.bm.jersey.service.syndata;

import com.emis.vi.bm.util.emisKeeper;
import com.emis.vi.bm.util.emisPropUtil;
import com.emis.vi.bm.util.emisUtil;
import com.emis.vi.bm.jersey.service.emisAbstractService;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 大屏点餐-同步后台数据接口
 */
public class emisBMSynDataImpl extends emisAbstractService {

    private final static String ACT_synPartData = "synPartData";  // 1.90 同步基础数据
    private final static String ACT_checkLogin = "checkLogin";  // 1.91 登录身份检查
    private String defaultAct = "checkLogin";

    /**
     * 设置默认act
     *
     * @param defaultAct
     */
    public void setDefaultAct(String defaultAct) {
        this.defaultAct = defaultAct;
    }

    @Override
    protected String postAction() {
        MultivaluedMap<String, String> req = parseRequest();
        // 获取请求Act
        String sAct = req.getFirst("act");
        // 当没有传act时取默认的defaultAct
        if (sAct == null || "".equals(sAct.trim())) {
            sAct = this.defaultAct;
        }
        // 选择响应业务
        if (ACT_synPartData.equalsIgnoreCase(sAct)) {
            return doSynPartData(req);
        } else if (ACT_checkLogin.equalsIgnoreCase(sAct)) {
            return doCheckLogin(req);
        }
        return null;
    }


    /**
     * 分类列表
     *
     * @param req request参数
     * @return 分类列表
     * @throws Exception
     */
    private String doSynPartData(MultivaluedMap<String, String> req) {
        String code = "";
        String msg = "";

        try {
            code = "0";
            msg = "成功";
        } catch (Exception ex) {
            code = "900";
            msg = "处理异常,请重试";
        }

        return "{\"code\":\"" + code + "\", \"msg\":\"" + msg + "\"}";
    }

    /**
     * 登录检查
     *
     * @param req request参数
     * @return
     * @throws Exception
     */
    private String doCheckLogin(MultivaluedMap<String, String> req) {
        String code = "";
        String msg = "";

        String sPosUrl = emisUtil.parseString(req.getFirst("sPosUrl"));
        String sNo = emisUtil.parseString(req.getFirst("sNo"));
        String idNo = emisUtil.parseString(req.getFirst("idNo"));
        String uId = emisUtil.parseString(req.getFirst("uId"));
        String uPwd = emisUtil.parseString(req.getFirst("uPwd"));

        try {
            String SME_URL = emisPropUtil.get("SME_URL");
            String S_NO = emisPropUtil.get("S_NO");
            String ID_NO = emisPropUtil.get("ID_NO");
            String POS_USERPWD = emisPropUtil.get("POS_USERPWD");
            String POS_USERID = emisPropUtil.get("POS_USERID");
            String BM_VERSION = emisPropUtil.get("BM_VERSION");

            HttpClient _oClient = null;
            int _iStatus = 0;
            Response resp = null;
            String respBody = "";
            try {
                _oClient = new HttpClient();
                _oClient.setConnectionTimeout(180000);
                _oClient.setTimeout(180000);
                PostMethod method = new PostMethod(sPosUrl + "/ws/wechatV3/bm/checkLogin");
                method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
                method.addParameter("sNo", sNo);
                method.addParameter("idNo", idNo);
                method.addParameter("uId", uId);
                method.addParameter("uPwd", uPwd);
                method.addParameter("bmVersion", BM_VERSION);

                for (MultivaluedMap.Entry<String, List<String>> entry : req.entrySet()) {
                    List ls = entry.getValue();
                    method.addParameter(entry.getKey(), ls.get(0).toString());
                }
                _iStatus = _oClient.executeMethod(method);
                resp = Response.ok(method.getResponseBodyAsString(), MediaType.APPLICATION_JSON).build();
                respBody = method.getResponseBodyAsString();
            } catch (Exception e) {
            }

            if (resp != null) {
                System.out.println("resp has data");
            } else {
                System.out.println("resp error");
            }

            System.out.println(respBody);
            JSONObject posResp = null;
            if (respBody == null || "".equals(respBody)) {
                return "{\"code\":\"800\",\"msg\":\"后台连线异常，请重试。\"}";
            } else {
                if (!emisUtil.isJSON(respBody.trim())) {
                    return "{\"code\":\"801\",\"msg\":\"后台连线异常，请重试。\"}";
                } else {
                    posResp = JSONObject.fromObject(respBody);
                    if (posResp == null || posResp.isEmpty()) {
                        return "{\"code\":\"802\",\"msg\":\"后台连线异常，请重试。\"}";
                    } else {
                        String posResp_code = getJsonString(posResp, "code");
                        if (!"0".equals(posResp_code) && !"00".equals(posResp_code)) {
                            return respBody;
                        }
                    }
                }
            }

            boolean firstLogin = false;
            if ("".equals(SME_URL) || "".equals(S_NO) || "".equals(ID_NO) || "".equals(POS_USERPWD) || "".equals(POS_USERID)) {
                // 系统参数不完整，不执行后续动作。
                firstLogin = true;
            }

            updEmisprop(sPosUrl, sNo, idNo, uId, uPwd);
            //emisProp.reload(context_);
            emisKeeper.getInstance().reloadEmisPropMap();

            if (firstLogin) {
                try {
                    // 首次登录成功，立即调用下载排程
          /*if (emisScheduleMgr.getInstance(context_).isExists("emisBMDownload")) {
            ClassLoader oClassLoader = Thread.currentThread().getContextClassLoader();
            Class obj = oClassLoader.loadClass("com.emis.schedule.epos.bm.emisBMDownload");
            emisTask task = (emisTask) (obj).newInstance();
            task.setName("emisBMDownload");
            task.setContext(context_);
            Thread t = new Thread(task);
            t.start();
          }*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            code = "0";
            msg = "成功";
        } catch (Exception ex) {
            code = "900";
            msg = "处理异常,请重试";
        }

        return "{\"code\":\"" + code + "\", \"msg\":\"" + msg + "\"}";
    }

    private String getJsonString(JSONObject jsonObj, String data) {
        String sReturn = "";
        try {
            if (jsonObj.has(data)) {
                sReturn = jsonObj.getString(data);
            }
        } catch (Exception ex) {
            sReturn = "";
            ex.printStackTrace();
        }
        return sReturn;
    }

    @Transactional
    public boolean updEmisprop(String sPosUrl, String sNo, String idNo, String uId, String uPwd) throws SQLException {
        String today = emisUtil.todayDateAD();
        // SME_URL
        int result = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement updEmispropStmt = con.prepareStatement("update emisprop set VALUE = ?, UPD_DATE = ? where NAME = ?");
                updEmispropStmt.clearParameters();
                updEmispropStmt.setString(1, sPosUrl);
                updEmispropStmt.setString(2, today);
                updEmispropStmt.setString(3, "SME_URL");
                return updEmispropStmt;
            }
        });
        if (result <= 0) {
            result = jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement insEmispropStmt = con.prepareStatement("insert into emisprop (NAME, VALUE, UPD_DATE) values (?, ?, ?)");
                    insEmispropStmt.clearParameters();
                    insEmispropStmt.setString(1, "SME_URL");
                    insEmispropStmt.setString(2, sPosUrl);
                    insEmispropStmt.setString(3, today);
                    return insEmispropStmt;
                }
            });
        }

        // S_NO
        String sqlStr = "update emisprop set VALUE = ?, UPD_DATE = ? where NAME = ?";
        result = jdbcTemplate.update(sqlStr, sNo, today, "S_NO");
        if (result <= 0) {
            sqlStr = "insert into emisprop (NAME, VALUE, UPD_DATE) values (?, ?, ?)";
            result = jdbcTemplate.update(sqlStr, "S_NO", sNo, today);
        }

        return true;
    }

}