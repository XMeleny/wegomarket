package com.example.wegomarket.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Purchase implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String productIdList;

    @Column(nullable = false)
    private String productAmountList;

    @Column(nullable = false)
    private double total;

    @Column(nullable = false)
    private String purchaseTime;

    public Purchase() {
    }

    public Purchase(Long userId, String productIdList, String productAmountList, double total, String purchaseTime) {
        this.userId = userId;
        this.productIdList = productIdList;
        this.productAmountList = productAmountList;
        this.total = total;
        this.purchaseTime = purchaseTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(String productIdList) {
        this.productIdList = productIdList;
    }

    public String getProductAmountList() {
        return productAmountList;
    }

    public void setProductAmountList(String productAmountList) {
        this.productAmountList = productAmountList;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
}