package com.emis.vi.dao;

import com.emis.vi.dto.OmsOrderDeliveryParam;
import com.emis.vi.dto.OmsOrderDetail;
import com.emis.vi.dto.OmsOrderQueryParam;
import com.emis.vi.model.OmsOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单自定义查询Dao
 */
public interface OmsOrderDao {
    /**
     * 条件查询订单
     */
    List<OmsOrder> getList(@Param("queryParam") OmsOrderQueryParam queryParam);

    /**
     * 批量发货
     */
    int delivery(@Param("list") List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(@Param("id") Long id);
}
