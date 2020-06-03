package com.emis.vi.dao;

import com.emis.vi.dto.PmsProductAttributeCategoryItem;

import java.util.List;

/**
 * 自定义商品属性分类Dao
 */
public interface PmsProductAttributeCategoryDao {
    /**
     * 获取商品属性分类，包括属性
     */
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
