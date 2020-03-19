package com.example.wegomarket.service.impl;

import com.example.wegomarket.model.Purchase;
import com.example.wegomarket.repository.PurchaseRepository;
import com.example.wegomarket.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Purchase getPurchaseById(long id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public List<Purchase> getPurchase() {
        return purchaseRepository.findAll();
    }

    @Override
    public List<Purchase> getPurchaseByUserId(long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    @Override
    public List<Purchase> getPurchaseByTime(String time) {
//        return null;
        return purchaseRepository.findPurchaseByPurchaseTimeIsLike("%" + time + "%");
    }

    @Override
    public List<Purchase> getPurchaseByTimeAndUserId(String time, long userId) {
        return purchaseRepository.findPurchaseByPurchaseTimeIsLikeAndUserId("%" + time + "%", userId);
    }


    @Override
    public void save(Purchase purchase) {
        //saveAndFlush: 能获取刚插入的记录的id并放在purchase对象中
        purchaseRepository.saveAndFlush(purchase);
    }

    @Override
    public void delete(long id) {
        purchaseRepository.deleteById(id);
    }

    @Override
    public void edit(Purchase purchase) {
        //may be not useful
        purchaseRepository.save(purchase);
    }
}
