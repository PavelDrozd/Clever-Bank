package com.clevertec.bankmanager.store.entity;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is entity class for account object in database.
 */
@Data
public class Account {

    /** ID used as primary key in database. */
    private Long id;

    /** Account number. */
    private Long number;

    /** The amount of money on the account. */
    private Double amount;

    /** The last date when the cashback was accrued. */
    private LocalDate cashbackLastDate;

    /** Link to the bank that this account belongs to. */
    private Bank bank;

    /** Link to the user who uses this account. */
    private User user;

    /** Lock is used for block this object to change the values of its fields. */
    @ToString.Exclude
    protected Lock lock = new ReentrantLock();
}
