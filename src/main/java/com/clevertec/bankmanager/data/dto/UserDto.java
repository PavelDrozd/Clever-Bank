package com.clevertec.bankmanager.data.dto;


import lombok.Data;

/**
 * Data transfer object class for bank entity.
 */
@Data
public class UserDto {

    /** ID used as primary key. */
    private Long id;

    /** User first name. */
    private String firstName;

    /** User last name. */
    private String lastName;
}
