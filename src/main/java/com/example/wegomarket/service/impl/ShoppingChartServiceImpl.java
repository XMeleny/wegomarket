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
    public List<ShoppingChart> getShoppingChartList() {
        return shoppingChartRepository.findAll();
    }

    @Override
    public ShoppingChart getShoppingChartById(long id) {
        return shoppingChartRepository.findById(id);
    }

    @Override
    public List<ShoppingChart> getShoppingChartListByUserId(long userId) {
        return shoppingChartRepository.findByUserId(userId);
    }

    @Override
    public ShoppingChart getShoppingChartByUserIdAndProductId(long userId, long productId) {
        return shoppingChartRepository.findByUserIdAndProductId(userId,productId);
    }



    @Override
    public void save(ShoppingChart shoppingChart) {
        shoppingChartRepository.save(shoppingChart);
    }

    @Override
    public void edit(ShoppingChart shoppingChart) {
        shoppingChartRepository.save(shoppingChart);
    }

    @Override
    public void delete(long id) {
        shoppingChartRepository.deleteById(id);

    }
}
