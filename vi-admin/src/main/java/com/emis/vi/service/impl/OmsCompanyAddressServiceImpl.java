package com.emis.vi.service.impl;

import com.emis.vi.mapper.OmsCompanyAddressMapper;
import com.emis.vi.model.OmsCompanyAddress;
import com.emis.vi.model.OmsCompanyAddressExample;
import com.emis.vi.service.OmsCompanyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 收货地址管理Service实现类
 */
@Service
public class OmsCompanyAddressServiceImpl implements OmsCompanyAddressService {
    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;
    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
