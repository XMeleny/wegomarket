package com.example.wegomarket.repository;

import com.example.wegomarket.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRepository  extends JpaRepository<Purchase, Long> {
    Purchase findById(long id);
    List<Purchase> findByUserId(long userId);
    List<Purchase> findPurchaseByPurchaseTimeIsLike(String purchaseTime);
    List<Purchase> findPurchaseByPurchaseTimeIsLikeAndUserId(String purchaseTime,long userId);
    void deleteById(long id);

}
