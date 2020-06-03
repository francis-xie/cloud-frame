package com.emis.vi.portal.service;

import com.emis.vi.portal.domain.OmsOrderReturnApplyParam;

/**
 * 订单退货管理Service
 */
public interface OmsPortalOrderReturnApplyService {
    /**
     * 提交申请
     */
    int create(OmsOrderReturnApplyParam returnApply);
}
