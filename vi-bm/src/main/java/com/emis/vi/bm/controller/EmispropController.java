package com.emis.vi.bm.controller;

import com.emis.vi.bm.mapper.EmispropMapper;
import com.emis.vi.bm.pojo.Emisprop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统参数管理Controller
 */
@RestController
@Api(tags = "EmispropController", description = "系统参数管理")
@RequestMapping("/emisprop")
public class EmispropController {
    @Autowired
    private EmispropMapper emispropMapper;

    @ApiOperation(value = "查询全部系统参数")
    @RequestMapping(name = "/getAll", method = RequestMethod.GET)
    public List<Emisprop> getAllEmisprop() {
        return emispropMapper.listAll();
    }
}
