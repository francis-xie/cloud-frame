package com.emis.vi.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.emis.vi.demo.dto.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 该服务类提供一个 Product 集合。
 * 需要注意的是，这里把 端口号 放进了产品信息里。这个数据服务会做成集群，
 * 那么访问者为了分辨到底是从哪个数据微服务取的数据，就需要提供个端口号，才能意识到是从不同的微服务得到的数据。
 */
@Service
public class ProductService {
    @Value("${server.port}")
    String port;

    public List<Product> listProducts() {
        List<Product> ps = new ArrayList<>();
        ps.add(new Product(1, "product a from port:" + port, 50));
        ps.add(new Product(2, "product b from port:" + port, 100));
        ps.add(new Product(3, "product c from port:" + port, 150));
        return ps;
    }
}
