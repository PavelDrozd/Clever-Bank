package com.clevertec.bankmanager.store.entity;

import lombok.Data;

/**
 * This is entity class for bank object in database.
 */
@Data
public class Bank {

    /** ID used as primary key in database. */
    private Long id;

    /** Bank name. */
    private String name;
}
