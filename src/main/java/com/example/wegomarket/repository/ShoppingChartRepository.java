package com.example.wegomarket.repository;

import com.example.wegomarket.model.ShoppingChart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingChartRepository extends JpaRepository<ShoppingChart, Long> {
    ShoppingChart findById(long id);
    List<ShoppingChart> findByUserId(long userId);
    void deleteById(long id);
    ShoppingChart findByUserIdAndProductId(long userId,long productId);


}
