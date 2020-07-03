package com.emis.vi.bm.jersey.resource;

import com.emis.vi.bm.jersey.service.ISomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * 类似spring的controller
 * 访问：http://localhost:8080/bm/resource/sayhi?msg=wolfcode
 */
@Component
@Path("resource")
public class SpringbootResource2 {

    @Autowired
    private ISomeService someService;

    @Path("sayhi2")
    @GET
    public String sayHi(@QueryParam("msg") String msg) {
        this.someService.sayHi(msg);
        return "success";
    }
}