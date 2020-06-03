package com.emis.vi.service.impl;

import com.emis.vi.mapper.CmsPrefrenceAreaMapper;
import com.emis.vi.model.CmsPrefrenceArea;
import com.emis.vi.model.CmsPrefrenceAreaExample;
import com.emis.vi.service.CmsPrefrenceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品优选Service实现类
 */
@Service
public class CmsPrefrenceAreaServiceImpl implements CmsPrefrenceAreaService {
    @Autowired
    private CmsPrefrenceAreaMapper prefrenceAreaMapper;

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return prefrenceAreaMapper.selectByExample(new CmsPrefrenceAreaExample());
    }
}
