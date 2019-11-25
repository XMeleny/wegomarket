package com.example.wegomarket.web;

import com.example.wegomarket.model.User;
import com.example.wegomarket.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.jws.WebParam;
import java.util.List;

@Controller
public class UserController {
    @Resource
    UserService userService;

    @RequestMapping("/")
    public String index(){
        return "redirect:/list";
    }

    @RequestMapping("/list")
    public String list(Model model){
        List<User> users=userService.getUserList();
        model.addAttribute("users", users);
        return "user/list";
    }

    @RequestMapping("/registerPage")
    public String registerPage(){
        return "user/registerPage";
    }

    @RequestMapping("/register")
    public String register(User user){
        userService.save(user);
        return "redirect:/list";
    }

    @RequestMapping("/delete")
    public String delete(long id){
        userService.delete(id);
        return "redirect:/list";
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
        return "redirect:/list";
    }

    @RequestMapping("/login")
    public String login(User user){
        //todo find
        return null;
    }
}
