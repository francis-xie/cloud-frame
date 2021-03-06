package com.emis.vi.demo.controller;

import com.emis.vi.common.api.CommonResult;
import com.emis.vi.demo.dto.UmsAdminLoginParam;
import com.emis.vi.demo.service.FeignAdminService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Feign调用vi-admin接口示例
 */
@Api(tags = "FeignAdminController", description = "Feign调用vi-admin接口示例")
@RestController
@RequestMapping("/feign/admin")
public class FeignAdminController {
//    @Autowired
//    private FeignAdminService adminService;
//
//    @PostMapping("/login")
//    public CommonResult login(@RequestBody UmsAdminLoginParam loginParam) {
//        return adminService.login(loginParam);
//    }
//
//    @GetMapping("/getBrandList")
//    public CommonResult getBrandList() {
//        return adminService.getList();
//    }
}
