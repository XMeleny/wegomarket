package com.example.wegomarket.web;

import com.example.wegomarket.model.Product;
import com.example.wegomarket.model.User;
import com.example.wegomarket.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ProductController {
    @Resource
    ProductService productService;

    @RequestMapping("/")
    public String index(){
        return "redirect:/productList";
    }

    @RequestMapping("/productList")
    public String productList(Model model){
        List<Product> products=productService.getProductList();
        model.addAttribute("products",products);
        return "product/productList";
    }

    @RequestMapping("/productListForAdmin")
    public String productListForAdmin(Model model, @ModelAttribute("adminName") String adminName){
        if(adminName.equals("admin")){
            List<Product> products=productService.getProductList();
            model.addAttribute("products",products);
            model.addAttribute("adminName","admin");
            return "product/productListForAdmin";
        }
        else return "redirect:/productList";

    }

    @RequestMapping("/productListForUser")
    public String productListForUser(Model model, @ModelAttribute("userId") long userId){
        List<Product> products=productService.getProductList();
        model.addAttribute("products",products);
        model.addAttribute("userId",userId);
        return "product/productListForUser";
    }

    @RequestMapping("/addProduct")
    public String addProduct(Product product, RedirectAttributes redirectAttributes)
    {
        productService.save(product);
        redirectAttributes.addFlashAttribute("adminName","admin");
        return "redirect:/productListForAdmin";
    }

    @RequestMapping("/addProductPage")
    public String addProductPage()
    {
        return "product/addProductPage";
    }

    @RequestMapping("/editProductPage")
    public String editProductPage(Model model,long id)
    {
        Product product=productService.findProductById(id);
        model.addAttribute("product",product);
        return "product/editProductPage";
    }

    @RequestMapping("/editProduct")
    public String editProduct(Product product)
    {
        productService.save(product);
        return"redirect:/productListForAdmin";
    }

    @RequestMapping("deleteProduct")
    public String deleteProduct(long id){
        productService.delete(id);
        return"redirect:/productListForAdmin";
    }




}
