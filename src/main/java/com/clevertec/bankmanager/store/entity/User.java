package com.clevertec.bankmanager.store.entity;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private List<Account> accounts;

    private void addAccount(Account account){
        accounts.add(account);
    }
}
