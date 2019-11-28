package com.example.wegomarket.web;

import com.example.wegomarket.model.Product;
import com.example.wegomarket.model.Purchase;
import com.example.wegomarket.model.User;
import com.example.wegomarket.service.ProductService;
import com.example.wegomarket.service.PurchaseService;
import com.example.wegomarket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Controller
public class UserController {
    @Resource
    UserService userService;
    @Resource
    PurchaseService purchaseService;
    @Resource
    ProductService productService;

    @RequestMapping("/admin")
    public String admin()
    {
        return "user/admin";
    }

    @RequestMapping("/adminLogin")
    public String adminLogin(Model model,String adminName,String passWord,RedirectAttributes redirectAttributes)
    {
        if(adminName.equals("admin")&&passWord.equals("admin"))
        {
            //删除失效订单，并加上相应库存
            checkUselessPurchase();

            //登录
            redirectAttributes.addFlashAttribute("adminName","admin");
            return "redirect:/productListForAdmin";
        }
        //todo：notify that the adminName or passWord is wrong
        return "user/admin";
    }

    @RequestMapping("/userList")
    public String userList(Model model,String adminName){
        if (adminName!=null) {
            if(adminName.equals("admin")) {
                List<User> users=userService.getUserList();
                model.addAttribute("users", users);
                model.addAttribute("adminName","admin");
                return "user/userList";
            }
        }
        return "redirect:/productList";

    }

    @RequestMapping("/registerPage")
    public String registerPage(){
        return "user/registerPage";
    }

    @RequestMapping("/register")
    public String register(User user,RedirectAttributes redirectAttributes){
        userService.save(user);
        redirectAttributes.addFlashAttribute("userId",user.getId());
        return "redirect:/productListForUser";
    }

    @RequestMapping("/delete")
    public String delete(long id){
        userService.delete(id);
        return "redirect:/userList";
    }

    @RequestMapping("/editPage")
    public String editPage(Model model,long id) {
        User user=userService.findUserById(id);
        model.addAttribute("user", user);
        return "user/editPage";
    }

    @RequestMapping("/edit")
    public String edit(User user){
        userService.edit(user);
        return "redirect:/userList";
    }

    @RequestMapping("/loginPage")
    public String loginPage(User user){
        return "user/loginPage";
    }

    @RequestMapping("/login")
    public String login(String email, String passWord, RedirectAttributes redirectAttributes){
        User user=userService.findUserByEmail(email);
        //邮箱存在，测试密码是否正确
        if (user!=null)//密码正确，登录成功
        {
            //删除失效订单
            if(user.getPassWord().equals(passWord)) {
                //删除失效订单，并加上相应库存
                checkUselessPurchase();

                //登录
                redirectAttributes.addFlashAttribute("userId",user.getId());
                return "redirect:/productListForUser";//how to add para in navigation
            }
            else
            {
                return "user/loginPage";
            }
        }
        //邮箱不存在，跳转到注册页面
        else{
            return "user/registerPage";
        }
    }

    private void checkUselessPurchase() {
        List<Purchase> purchases=purchaseService.getPurchase();
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar calendar = new GregorianCalendar();
        Date today=new Date();
        for(Purchase purchase:purchases)
        {
            try {
                Date startDay= sdf.parse(purchase.getPurchaseTime());
                calendar.setTime(startDay);
                calendar.add(Calendar.DATE,1);
                Date endDay=calendar.getTime();
                if(today.after(endDay)&& !purchase.isOk()){
                    String[] productIds=purchase.getProductIdList().split(",");
                    String[] productAmounts=purchase.getProductAmountList().split(",");
                    for(int i=0;i<productIds.length;i++)
                    {
                        Product product=productService.findProductById(Long.parseLong(productIds[i]));
                        product.setStock(product.getStock()+Integer.parseInt(productAmounts[i]));
                        productService.save(product);
                    }
                    purchaseService.delete(purchase.getId());

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/selfEditPage")
    public String selfEdit(Model model, @ModelAttribute("userId") long userId)
    {
        User user=userService.findUserById(userId);
        model.addAttribute("user",user);
        return "user/selfEditPage";
    }

    @RequestMapping("/editSelf")
    public String editSelf(User user,RedirectAttributes redirectAttributes){
        System.out.println("here in function edit self");
        userService.edit(user);
        redirectAttributes.addFlashAttribute("userId",user.getId());
        return "redirect:/productListForUser";
    }

}
