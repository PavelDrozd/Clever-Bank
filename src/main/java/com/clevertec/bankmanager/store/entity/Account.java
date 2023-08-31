package com.clevertec.bankmanager.store.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class Account {

    private Long id;

    private Long number;

    private Double amount;

    private LocalDate cashbackLastDate;

    private Bank bank;

    private User user;

    private Lock lock = new ReentrantLock();
}
