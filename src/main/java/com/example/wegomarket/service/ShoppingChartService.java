package com.example.wegomarket.service;

import com.example.wegomarket.model.ShoppingChart;

import java.util.List;

public interface ShoppingChartService {
    public List<ShoppingChart> getShoppingChartList();
    public List<ShoppingChart> getShoppingChartListByUserId(long userId);
    public ShoppingChart getShoppingChartByUserIdAndProductId(long userId,long productId);
    public ShoppingChart getShoppingChartById(long id);

    public void save(ShoppingChart shoppingChart);
    public void edit(ShoppingChart shoppingChart);
    public void delete(long id);

 }
