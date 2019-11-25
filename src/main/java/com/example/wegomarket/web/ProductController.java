package com.example.wegomarket.web;

import com.example.wegomarket.model.Product;
import com.example.wegomarket.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ProductController {
    @Resource
    ProductService productService;

    @RequestMapping("/productList")
    public String productList(Model model){
        List<Product> products=productService.getProductList();
        model.addAttribute("products",products);
        return "product/productList";
    }


}
