package com.kiduyu.meshproject.a2nkbooking.Model;

import com.google.gson.Gson;

/**
 * Class which is used to represent the User of the Application
 */
public class User {
    private String userId;
    private double balance;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String password;

    public User(){

    }
    public User(String userId, double balance, String name, String mobile, String email, String address, String password) {
        this.userId = userId;
        this.balance = balance;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
