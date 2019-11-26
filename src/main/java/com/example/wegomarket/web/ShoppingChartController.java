package com.example.wegomarket.web;

import com.example.wegomarket.model.ShoppingChart;
import com.example.wegomarket.repository.ShoppingChartRepository;
import com.example.wegomarket.service.ShoppingChartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class ShoppingChartController {
    @Resource
    ShoppingChartService shoppingChartService;

    @RequestMapping("/addShoppingChart")
    public String addShoppingChart(ShoppingChart shoppingChart, RedirectAttributes redirectAttributes)
    {
        shoppingChartService.save(shoppingChart);
        redirectAttributes.addAttribute("userId",shoppingChart.getUserId());
        return "redirect:/productListForUser";
    }

    @RequestMapping("/shoppingChartList")
    public String shoppingChartList(Model model, long userId){
        List<ShoppingChart> shoppingCharts=shoppingChartService.getShoppingChartByUserId(userId);
        model.addAttribute("shoppingCharts" ,shoppingCharts);
        model.addAttribute("userId",userId);
        return "shoppingChart/shoppingChartList";
    }

}
