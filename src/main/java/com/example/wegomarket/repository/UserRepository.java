package com.example.wegomarket.repository;

import com.example.wegomarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    User findByEmail(String email);
    void deleteById(long id);
}
