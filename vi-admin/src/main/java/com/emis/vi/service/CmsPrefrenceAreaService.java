package com.emis.vi.service;

import com.emis.vi.model.CmsPrefrenceArea;

import java.util.List;

/**
 * 优选专区Service
 */
public interface CmsPrefrenceAreaService {
    /**
     * 获取所有优选专区
     */
    List<CmsPrefrenceArea> listAll();
}
