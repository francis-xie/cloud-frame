package com.emis.vi.dao;

import com.emis.vi.dto.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * 商品分类自定义Dao
 */
public interface PmsProductCategoryDao {
    /**
     * 获取商品分类包括子分类
     */
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
