package com.emis.vi.bm.jersey.resource;

import com.emis.vi.bm.jersey.config.AppConfig;
import com.emis.vi.bm.jersey.service.IEmisService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

/**
 * 大屏点餐 API接口
 * User: harry
 * Date: 2019/06/20
 */
@Path("/bm")
public class emisWebServiceEntry {
    @Context
    protected ServletContext context_;

    private final String RET = "{\"code\":\"-1\",\"msg\":\"unknown\"}";

    /**
     * Call Service by POST METHOD
     * /XX/ws/bm/part/list (post: JSON Data)
     *
     * @param sType
     * @param sAction
     * @param request
     * @return
     */
    @POST
    @Path("/{type}/{action}")
    public Response post(@PathParam("type") String sType, @PathParam("action") String sAction, @Context Request request) {
        String sRet = null;
        // 实现逻辑（建议独立的处理类
        //IEmisService service = (IEmisService) BeanUtil.getBean("bm", sType + "_" + sAction);
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        IEmisService service = (IEmisService) context.getBean(sType + "_" + sAction);
        if (service != null) {
            service.setServletContext(context_);
            service.setRequest(request);
            try {
                sRet = service.doAction();
            } catch (Exception e) {
            }
        }
        if (sRet == null || "".equals(sRet.trim())) {
            sRet = RET;
        }
        // 返回结果
        return Response.ok(sRet, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Call Service by GET METHOD
     * /XX/ws/bm/part/list (post: JSON Data)
     *
     * @param sType
     * @param sAction
     * @param request
     * @return
     */
    @GET
    @Path("/{type}/{action}")
    public Response get(@PathParam("type") String sType, @PathParam("action") String sAction, @Context Request request) {
        // 返回结果
        return post(sType, sAction, request);
    }

}