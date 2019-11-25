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
        return null;
    }

    @Override
    public List<Purchase> getPurchaseByUserId(long userId) {
        return null;
    }

    @Override
    public void save(Purchase purchase) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public void edit(Purchase purchase) {

    }
}
