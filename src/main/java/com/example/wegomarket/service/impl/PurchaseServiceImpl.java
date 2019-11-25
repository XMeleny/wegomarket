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
    public List<Purchase> getPurchase() {
        return purchaseRepository.findAll();
    }

    @Override
    public List<Purchase> getPurchaseByUserId(long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    @Override
    public void save(Purchase purchase) {
        purchaseRepository.save(purchase);

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