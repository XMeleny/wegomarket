package com.example.wegomarket.service.impl;

import com.example.wegomarket.model.ShoppingChart;
import com.example.wegomarket.repository.ShoppingChartRepository;
import com.example.wegomarket.service.ShoppingChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingChartServiceImpl implements ShoppingChartService {
    @Autowired
    private ShoppingChartRepository shoppingChartRepository;

    @Override
    public List<ShoppingChart> getShoppingChart() {
        return null;
    }

    @Override
    public List<ShoppingChart> getShoppingChartByUserId(long userId) {
        return null;
    }

    @Override
    public void save(ShoppingChart shoppingChart) {

    }

    @Override
    public void edit(ShoppingChart shoppingChart) {

    }

    @Override
    public void delete(long Id) {

    }
}
