package com.clevertec.bankmanager.data.dto;

import lombok.Data;

/**
 * Data transfer object class for bank entity.
 */
@Data
public class BankDto {

    /** ID used as primary key. */
    private Long id;

    /** Bank name. */
    private String name;
}
