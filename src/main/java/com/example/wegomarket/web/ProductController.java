package com.example.wegomarket.web;

import com.example.wegomarket.Util;
import com.example.wegomarket.model.Product;
import com.example.wegomarket.service.ProductService;
import com.example.wegomarket.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ProductController {

    private static final Logger LOG = LoggerFactory.getLogger(Product.class);

    @Resource
    ProductService productService;

    @Resource
    UserService userService;

    @RequestMapping("/")
    public String index() {
        return "redirect:/productList";
    }

    @RequestMapping("/productList")
    public String productList(Model model) {
        List<Product> products = productService.getProductList();
        model.addAttribute("products", products);
        return "product/productList";
    }

    @RequestMapping("/productListForAdmin")
    public String productListForAdmin(Model model, @ModelAttribute("adminName") String adminName) {
        if (adminName.equals("admin")) {
            List<Product> products = productService.getProductList();
            model.addAttribute("products", products);
            model.addAttribute("adminName", "admin");
            return "product/productListForAdmin";
        } else return "redirect:/productList";

    }

    @RequestMapping("/productListForUser")
    public String productListForUser(Model model, @ModelAttribute("userId") long userId) {
        Util.refreshRecommend(userService, userId);

        List<Product> products = productService.getProductList();
        model.addAttribute("products", products);
        model.addAttribute("userId", userId);
        int toBuy = userService.findUserById(userId).getToBuy();
        model.addAttribute("toBuy", toBuy);

        return "product/productListForUser";
    }

    @RequestMapping("/addProduct")
    public String addProduct(Product product, RedirectAttributes redirectAttributes) {
        productService.save(product);
        LOG.info("admin add a product: " + product.getName());
        redirectAttributes.addFlashAttribute("adminName", "admin");
        return "redirect:/productListForAdmin";
    }

    @RequestMapping("/addProductPage")
    public String addProductPage() {
        return "product/addProductPage";
    }

    @RequestMapping("/editProductPage")
    public String editProductPage(Model model, long id) {
        Product product = productService.findProductById(id);
        model.addAttribute("product", product);
        return "product/editProductPage";
    }

    @RequestMapping("/editProduct")
    public String editProduct(Product product, RedirectAttributes redirectAttributes) {
        productService.save(product);
        redirectAttributes.addFlashAttribute("adminName", "admin");
        LOG.info("admin edit a product: " + product.getName());
        return "redirect:/productListForAdmin";
    }

    @RequestMapping("deleteProduct")
    public String deleteProduct(long id, RedirectAttributes redirectAttributes) {
        Product product = productService.findProductById(id);
        productService.delete(id);
        redirectAttributes.addFlashAttribute("adminName", "admin");
        LOG.info("admin delete a product: " + product.getName());
        return "redirect:/productListForAdmin";
    }
}
