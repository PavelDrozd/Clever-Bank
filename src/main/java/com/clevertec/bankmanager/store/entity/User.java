package com.clevertec.bankmanager.store.entity;

import lombok.Data;

/**
 * This is entity class for user object in database
 */
@Data
public class User {

    /** ID used as primary key in database */
    private Long id;

    /** User first name */
    private String firstName;

    /** User last name */
    private String lastName;
}
