package com.example.wegomarket.web;

import com.example.wegomarket.model.Product;
import com.example.wegomarket.model.Purchase;
import com.example.wegomarket.model.ShoppingChart;
import com.example.wegomarket.service.ProductService;
import com.example.wegomarket.service.PurchaseService;
import com.example.wegomarket.service.ShoppingChartService;

import com.example.wegomarket.util;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class PurchaseController {
    @Resource
    PurchaseService purchaseService;

    @Resource
    ShoppingChartService shoppingChartService;

    @Resource
    ProductService productService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @RequestMapping("/addPurchase")
    public String addPurchase(@RequestParam("shoppingChartIds") List<String> shoppingChartIds, RedirectAttributes redirectAttributes)
    {
        //todo：邮箱验证，先减库存，验证不成功（超时则加上库存，避免验证成功却无库存可减）
        //todo：生成验证码
        //before requesting, shoppingChartIds is not null

        //将信息填好，并减少库存
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String purchaseTime=formatter.format(new Date());

        double total=0;
        long userId=0;

        List<String> productIds=new ArrayList<>();
        List<String> productAmounts=new ArrayList<>();

        long productId;
        int productAmount;
        int productStock;
        double productPrice;

        boolean canBuy=true;

        String checkCode=util.makeCheckCode();
        List<ShoppingChart> shoppingCharts=new ArrayList<>();

        for(String shoppingChartId :shoppingChartIds)
        {
            ShoppingChart shoppingChart=shoppingChartService.getShoppingChartById(Integer.parseInt(shoppingChartId));
            shoppingCharts.add(shoppingChart);

            userId=shoppingChart.getUserId();

            productId=shoppingChart.getProductId();
            productAmount=shoppingChart.getAmount();
            productPrice=productService.findProductById(productId).getPrice();
            productStock=productService.findProductById(productId).getStock();
            //check the stock
            if (productAmount>productStock)
            {
                canBuy=false;
                break;
            }

            productIds.add(String.valueOf(productId));
            productAmounts.add(String.valueOf(productAmount));
            total += productAmount * productPrice;
        }

        if(canBuy) {
            //订单详细信息
            String productIdList = StringUtils.join(productIds, ',');
            String productAmountList = StringUtils.join(productAmounts, ',');

            Purchase purchase = new Purchase();
            purchase.setUserId(userId);
            purchase.setProductIdList(productIdList);
            purchase.setProductAmountList(productAmountList);
            purchase.setTotal(total);
            purchase.setPurchaseTime(purchaseTime);
            purchase.setCheckCode(checkCode);

            purchaseService.save(purchase);

            //从购物车中删除记录
            for(String shoppingChartId :shoppingChartIds){
                shoppingChartService.delete(Long.parseLong(shoppingChartId));
            }

            //减少库存
            for(ShoppingChart shoppingChart :shoppingCharts){
                Product product =productService.findProductById(shoppingChart.getProductId());
                product.setStock(product.getStock()-shoppingChart.getAmount());
                productService.save(product);
            }

            //发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(username);
            message.setTo("865622793@qq.com");
            message.setSubject("确认下单");
            message.setText(checkCode);
            javaMailSender.send(message);
        }

        else{
            System.out.println("库存不够哦！");
            //todo :else alert the stock is not enough
        }

        redirectAttributes.addFlashAttribute("userId",userId);
        return "redirect:/productListForUser";

    }

    @RequestMapping("/purchaseList")
    public String purchaseList(Model model,String adminName)
    {
        if (adminName!=null){
            if(adminName.equals("admin")){
                model.addAttribute("purchases",purchaseService.getPurchase());
                model.addAttribute("adminName","admin");
                return "purchase/purchaseList";
            }
        }
        return "redirect:/productList";
    }

    @RequestMapping("/purchaseListForUser")
    public String purchaseListForUser(@ModelAttribute("userId") long userId,Model model){
        model.addAttribute("userId",userId);
        model.addAttribute("purchases",purchaseService.getPurchaseByUserId(userId));
        return "purchase/purchaseListForUser";
    }

    @RequestMapping("/ensurePurchase")
    public String checkPurchase(long purchaseId,String checkCode,RedirectAttributes redirectAttributes )
    {
        Purchase purchase=purchaseService.getPurchaseById(purchaseId);
        if(checkCode.equals(purchase.getCheckCode())){
            purchase.setOk(true);
            purchaseService.save(purchase);
        }

        redirectAttributes.addFlashAttribute("userId",purchase.getUserId());
        return "redirect:/purchaseListForUser";

    }
}
