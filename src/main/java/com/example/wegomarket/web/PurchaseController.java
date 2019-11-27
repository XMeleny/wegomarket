package com.example.wegomarket.web;

import com.example.wegomarket.model.Purchase;
import com.example.wegomarket.model.ShoppingChart;
import com.example.wegomarket.service.ProductService;
import com.example.wegomarket.service.PurchaseService;
import com.example.wegomarket.service.ShoppingChartService;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @RequestMapping("/addPurchase")
    public String addPurchase(@RequestParam("shoppingChartIds") List<String> shoppingChartIds, RedirectAttributes redirectAttributes)
    {
        //before requesting, shoppingChartIds is not null

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

        for(String item :shoppingChartIds)
        {
            ShoppingChart shoppingChart=shoppingChartService.getShoppingChartById(Integer.parseInt(item));

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
            String productIdList = StringUtils.join(productIds, ',');
            String productAmountList = StringUtils.join(productAmounts, ',');

            Purchase purchase = new Purchase();
            purchase.setUserId(userId);
            purchase.setProductIdList(productIdList);
            purchase.setProductAmountList(productAmountList);
            purchase.setTotal(total);
            purchase.setPurchaseTime(purchaseTime);

            purchaseService.save(purchase);

            //delete record in shoppingChart
            for(String item :shoppingChartIds){
                shoppingChartService.delete(Long.parseLong(item));
            }
        }
        //todo :else alert the stock is not enough

        redirectAttributes.addFlashAttribute("userId",userId);
        return "redirect:/productListForUser";

    }

    @RequestMapping("/purchaseList")
    public String purchaseList(Model model,String adminName)
    {
        if (adminName!=null)        {
            if(adminName.equals("admin")){
                model.addAttribute("purchases",purchaseService.getPurchase());
                model.addAttribute("adminName","admin");
                return "purchase/purchaseList";
            }
        }
        return "redirect:/productList";


    }
}
