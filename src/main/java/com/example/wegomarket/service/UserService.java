package com.example.wegomarket.service;

import com.example.wegomarket.model.User;
import java.util.List;

public interface UserService {
    public List<User> getUserList();

    public User findUserById(long id);

    public User findUserByEmail(String email);

    public void save(User user);

    public void edit(User user);

    public void delete(long id);
}
