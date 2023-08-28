package com.clevertec.bankmanager.data.dto;

import com.clevertec.bankmanager.store.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class BankDto {

    private Long id;

    private String name;

    private List<User> users;
}
