package com.clevertec.bankmanager.data.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * Data transfer object class for account entity.
 */
@Data
public class AccountDto {

    /** ID used as primary key. */
    private Long id;

    /** Account number. */
    private Long number;

    /** The amount of money on the account. */
    private Double amount;

    /** The last date when the cashback was accrued. */
    private LocalDate cashbackLastDate;

    /** Link to the bank that this account belongs to. */
    private BankDto bank;

    /** Link to the user who uses this account. */
    private UserDto user;
}
