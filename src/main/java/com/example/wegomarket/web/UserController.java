package com.example.wegomarket.web;

import com.example.wegomarket.model.User;
import com.example.wegomarket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserController {
    @Resource
    UserService userService;

    @RequestMapping("/")
    public String index(){
        return "redirect:/userList";
    }

    @RequestMapping("/userList")
    public String userList(Model model){
        List<User> users=userService.getUserList();
        model.addAttribute("users", users);
        return "user/userList";
    }

    @RequestMapping("/registerPage")
    public String registerPage(){
        return "user/registerPage";
    }

    @RequestMapping("/register")
    public String register(User user){
        userService.save(user);
        return "redirect:/userList";
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
    public String login(String email,String passWord){
        User user=userService.findUserByEmail(email);
        //邮箱存在，测试密码是否正确
        if (user!=null)
        {
            //密码正确，登录成功
            if(user.getPassWord().equals(passWord))
                return "redirect:/userList";
            else
                return "user/loginPage";
        }
        //邮箱不存在，跳转到注册页面
        else{
            return "user/registerPage";
        }
    }
}
