package com.clevertec.bankmanager.data.dto;


import com.clevertec.bankmanager.store.entity.Account;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private List<Account> accounts;
}
