package com.emis.vi.bm.jersey.service.impl;

import com.emis.vi.bm.jersey.service.ISomeService;
import org.springframework.stereotype.Service;

@Service
public class SomeServiceImpl implements ISomeService {
    @Override
    public void sayHi(String msg) {
        System.out.println(msg);
    }
}