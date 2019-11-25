package com.example.wegomarket.service;

import com.example.wegomarket.model.ShoppingChart;

import java.util.List;

public interface ShoppingChartService {
    public List<ShoppingChart> getShoppingChart();
    public List<ShoppingChart> getShoppingChartByUserId(long userId);

    public void save(ShoppingChart shoppingChart);
    public void edit(ShoppingChart shoppingChart);
    public void delete(long id);

 }
