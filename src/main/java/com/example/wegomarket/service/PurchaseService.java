package com.example.wegomarket.service;

import com.example.wegomarket.model.Purchase;
import com.example.wegomarket.model.User;

import java.util.List;

public interface PurchaseService {
    public List<Purchase> getPurchase();
    public List<Purchase> getPurchaseByUserId(long userId);
    public Purchase getPurchaseById(long id);
    public List<Purchase> getPurchaseByTime(String time);
    public List<Purchase> getPurchaseByTimeAndUserId(String time,long userId);

    public void save(Purchase purchase);
    public void delete(long id);
    public void edit(Purchase purchase);
}
