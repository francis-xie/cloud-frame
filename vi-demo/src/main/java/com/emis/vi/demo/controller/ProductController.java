package com.emis.vi.demo.controller;

import java.util.List;

import com.emis.vi.demo.dto.Product;
import com.emis.vi.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping("/products")
    public Object products(Model m) {
        List<Product> ps = productService.listProducts();
        m.addAttribute("ps", ps);
        return "products";
    }

    @RequestMapping("/productss")
    @ResponseBody
    public Object productss() {
        List<Product> ps = productService.listProducts();
        //把 Product 集合转换成 json 数组返回
        return ps;
    }
}
