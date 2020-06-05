package com.emis.vi.demo.controller;

import com.emis.vi.common.api.CommonResult;
import com.emis.vi.demo.service.FeignPortalService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Feign调用vi-portal接口示例
 */
@Api(tags = "FeignPortalController", description = "Feign调用vi-portal接口示例")
@RestController
@RequestMapping("/feign/portal")
public class FeignPortalController {

//    @Autowired
//    private FeignPortalService portalService;
//
//    @PostMapping("/login")
//    public CommonResult login(@RequestParam String username, @RequestParam String password) {
//        return portalService.login(username,password);
//    }
//
//    @GetMapping("/cartList")
//    public CommonResult cartList() {
//        return portalService.list();
//    }
}
