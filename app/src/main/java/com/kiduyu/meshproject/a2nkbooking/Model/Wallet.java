package com.kiduyu.meshproject.a2nkbooking.Model;

public class Wallet {
    String uid,name,balance,pin;

    public Wallet(String uid, String name, String balance, String pin) {
        this.uid = uid;
        this.name = name;
        this.balance = balance;
        this.pin = pin;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
