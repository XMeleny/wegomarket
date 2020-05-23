package com.example.wegomarket.model;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;

//todo: 加密密码存储
@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = false)
    private String passWord;
    @Column(nullable = true, unique = true)
    private String nickName;
    @Column
    private int buy = 0;
    @Column
    private int toBuy = 0;

    public User() {
    }

    public User(String email, String passWord, String nickName, int buy, int toBuy) {
        this.email = email;
        this.passWord = passWord;
        this.nickName = nickName;
        this.buy = buy;
        this.toBuy = toBuy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getBuy() {
        return buy;
    }

    public void setBuy(int buy) {
        this.buy = buy;
    }

    public int getToBuy() {
        return toBuy;
    }

    public void setToBuy(int toBuy) {
        this.toBuy = toBuy;
    }
}
