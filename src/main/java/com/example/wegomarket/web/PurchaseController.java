package com.example.wegomarket.web;

import com.example.wegomarket.model.Product;
import com.example.wegomarket.model.Purchase;
import com.example.wegomarket.model.ShoppingChart;
import com.example.wegomarket.service.ProductService;
import com.example.wegomarket.service.PurchaseService;
import com.example.wegomarket.service.ShoppingChartService;

import com.example.wegomarket.Util;
import com.example.wegomarket.service.UserService;
import org.apache.tomcat.util.buf.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class PurchaseController {
    private static final Logger LOG = LoggerFactory.getLogger(Purchase.class);

    @Resource
    PurchaseService purchaseService;

    @Resource
    ShoppingChartService shoppingChartService;

    @Resource
    ProductService productService;

    @Resource
    UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @RequestMapping("/addPurchase")
    public String addPurchase(String[] shoppingChartIds, RedirectAttributes redirectAttributes)
    {
        System.out.println("the size is: "+shoppingChartIds.length);

        //进行操作之前已经保证shoppingChartIds不为空
        //将信息填好
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String purchaseTime=formatter.format(new Date());

        double total=0;
        long userId=0;

        List<String> productIds=new ArrayList<>();
        List<String> productNames=new ArrayList<>();
        List<String> productAmounts=new ArrayList<>();
        List<String> productPrices=new ArrayList<>();

        Product tempProduct;

        long productId;
        String productName;
        int productAmount;
        double productPrice;
        int productStock;

        boolean canBuy=true;

        String checkCode= Util.makeCheckCode();
        List<ShoppingChart> shoppingCharts=new ArrayList<>();

        for(String shoppingChartId :shoppingChartIds)
        {
            ShoppingChart shoppingChart=shoppingChartService.getShoppingChartById(Integer.parseInt(shoppingChartId));
            shoppingCharts.add(shoppingChart);

            userId=shoppingChart.getUserId();

            productId=shoppingChart.getProductId();
            tempProduct=productService.findProductById(productId);
            productName=tempProduct.getName();
            productPrice=productService.findProductById(productId).getPrice();
            productAmount=shoppingChart.getAmount();
            productStock=productService.findProductById(productId).getStock();

            //check the stock
            if (productAmount>productStock)
            {
                canBuy=false;
                break;
            }

            productIds.add(String.valueOf(productId));
            productNames.add(productName);
            productAmounts.add(String.valueOf(productAmount));
            productPrices.add(String.valueOf(productPrice));

            total += productAmount * productPrice;
        }

        if(canBuy) {
            //订单详细信息
            String productIdList = StringUtils.join(productIds, ',');
            String productNameList = StringUtils.join(productNames, ',');
            String productAmountList = StringUtils.join(productAmounts, ',');
            String productPriceList = StringUtils.join(productPrices, ',');

            Purchase purchase = new Purchase();
            purchase.setUserId(userId);
            purchase.setProductIdList(productIdList);
            purchase.setProductNameList(productNameList);
            purchase.setProductAmountList(productAmountList);
            purchase.setProductPriceList(productPriceList);
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
            message.setTo(userService.findUserById(purchase.getUserId()).getEmail());
            message.setSubject("确认下单");
            message.setText("你在"+purchase.getPurchaseTime()+"购买了商品，请于明晚24:00前确认订单。\n" +
                    "验证码为："+checkCode+"。\n"+
                    "订单号为："+purchase.getId());
            javaMailSender.send(message);

            //purchase.getId(): 获得刚插入的记录的自增id
            LOG.info("user"+userId+"just add purchase"+purchase.getId());
        }

        else{
            System.out.println("库存不够哦！");
        }

        redirectAttributes.addFlashAttribute("userId",userId);
        return "redirect:/purchaseListForUser";

    }

    @RequestMapping("/purchaseList")
    public String purchaseList(Model model,String adminName)
    {
        if (adminName!=null){
            if(adminName.equals("admin")){
                getTime(model);
                List<Purchase> purchases=purchaseService.getPurchase();
                double summary=0;
                for (int i=0;i<purchases.size();i++)
                {
                    summary+=purchases.get(i).getTotal();
                }
                model.addAttribute("purchases",purchases);
                model.addAttribute("summary",summary);
                model.addAttribute("adminName","admin");
                return "purchase/purchaseList";
            }
        }
        return "redirect:/productList";
    }

    @RequestMapping("purchaseListForSpecific")
    public String purchaseListForSpecific(Model model,String adminName,String year,String month,String day,String mode,RedirectAttributes redirectAttributes){
        if (adminName!=null){
            if(adminName.equals("admin")){
                List<Purchase> purchases=new ArrayList<>();
                double summary=0;
                if(mode.equals("year")){
                    purchases=purchaseService.getPurchaseByTime(year+"-");
                }
                else if(mode.equals("month")){
                    purchases=purchaseService.getPurchaseByTime(year+"-"+month+"-");
                }
                else if(mode.equals("day")){
                    purchases=purchaseService.getPurchaseByTime(year+"-"+month+"-"+day);
                }

                for (int i=0;i<purchases.size();i++)
                {
                    summary+=purchases.get(i).getTotal();
                }

                model.addAttribute("adminName","admin");
                model.addAttribute("purchases",purchases);
                model.addAttribute("summary",summary);
                model.addAttribute("year",year);
                model.addAttribute("month",month);
                model.addAttribute("day",day);
                return "purchase/purchaseListForSpecific";
            }
        }
        return "redirect:/productList";
    }

    @RequestMapping("/purchaseListForUser")
    public String purchaseListForUser(@ModelAttribute("userId") long userId,Model model){
        getTime(model);
        model.addAttribute("userId",userId);
        model.addAttribute("purchases",purchaseService.getPurchaseByUserId(userId));
        return "purchase/purchaseListForUser";
    }

    private void getTime(Model model) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tempDate = sdf.format(date);

        String[] nums =tempDate .split("-");

        model.addAttribute("year",nums[0]);
        model.addAttribute("month",nums[1]);
        model.addAttribute("day",nums[2]);
    }

    @RequestMapping("/purchaseListForUserSpecific")
    public String purchaseListForUserSpecific(@ModelAttribute("userId") long userId,Model model,String year,String month,String day,String mode){
        model.addAttribute("userId",userId);
        model.addAttribute("year",year);
        model.addAttribute("month",month);
        model.addAttribute("day",day);
        if(mode.equals("year"))
        {
            System.out.println("in specific for user, year");
            model.addAttribute("purchases",purchaseService.getPurchaseByTimeAndUserId(year+"-",userId));
        }
        else if(mode.equals("month")){
            System.out.println("in specific for user, month");
            model.addAttribute("purchases",purchaseService.getPurchaseByTimeAndUserId(year+"-"+month+"-",userId));
        }
        else if(mode.equals("day")){
            System.out.println("in specific for user, day");
            model.addAttribute("purchases",purchaseService.getPurchaseByTimeAndUserId(year+"-"+month+"-"+day,userId));
        }

//        model.addAttribute("purchases",purchaseService.getPurchaseByUserId(userId));//todo
        return"purchase/purchaseListForUserSpecific";
    }

    @RequestMapping("/ensurePurchase")
    public String checkPurchase(long purchaseId,String checkCode,RedirectAttributes redirectAttributes )
    {
        Purchase purchase=purchaseService.getPurchaseById(purchaseId);
        if(checkCode.equals(purchase.getCheckCode())){
            purchase.setOk(true);
            purchaseService.save(purchase);
        }

        LOG.info("user: "+purchase.getUserId()+" just ensure purchase: " +purchaseId);

        redirectAttributes.addFlashAttribute("userId",purchase.getUserId());
        return "redirect:/purchaseListForUser";

    }
}
