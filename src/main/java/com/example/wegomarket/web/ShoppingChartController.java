package com.example.wegomarket.web;

import com.example.wegomarket.model.Product;
import com.example.wegomarket.model.ShoppingChart;
import com.example.wegomarket.service.ProductService;
import com.example.wegomarket.service.ShoppingChartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShoppingChartController {
    @Resource
    ShoppingChartService shoppingChartService;

    @Resource
    ProductService productService;

    @RequestMapping(value = "/addShoppingChart" )
    public String addShoppingChart(ShoppingChart shoppingChart, RedirectAttributes redirectAttributes)
    {
        ShoppingChart originShoppingChart=shoppingChartService.getShoppingChartByUserIdAndProductId(shoppingChart.getUserId(),shoppingChart.getProductId());
        if(originShoppingChart!=null)
        {
            originShoppingChart.setAmount(originShoppingChart.getAmount()+1);
            shoppingChartService.save(originShoppingChart);
        }
        else{
            shoppingChartService.save(shoppingChart);
        }

        redirectAttributes.addFlashAttribute("userId",shoppingChart.getUserId());
        return "redirect:/productListForUser";
    }

    @RequestMapping("/shoppingChartList")
    public String shoppingChartList(Model model, @ModelAttribute("userId") long userId){
        List<ShoppingChart> shoppingCharts=shoppingChartService.getShoppingChartListByUserId(userId);

        Map<ShoppingChart, Product> map=new HashMap<>();
        for (ShoppingChart s :shoppingCharts){
            map.put(s,productService.findProductById(s.getProductId()));
        }

        model.addAttribute("userId",userId);
        model.addAttribute("map",map);
        return "shoppingChart/shoppingChartList";
    }

    @RequestMapping("/deleteShoppingChart")
    public String deleteShoppingChart(long shoppingChartId, long userId, RedirectAttributes redirectAttributes)
    {
        shoppingChartService.delete(shoppingChartId);
        redirectAttributes.addFlashAttribute("userId",userId);
        return "redirect:/shoppingChartList";
    }

}
