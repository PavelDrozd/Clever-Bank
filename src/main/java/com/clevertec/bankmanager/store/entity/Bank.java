package com.clevertec.bankmanager.store.entity;

import lombok.Data;

import java.util.List;

@Data
public class Bank {

    private Long id;

    private String name;

    private List<User> users;

    public void addUser(User user){
        users.add(user);
    }
}
