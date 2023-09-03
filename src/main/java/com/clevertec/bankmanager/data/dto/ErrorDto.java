package com.clevertec.bankmanager.data.dto;

import lombok.Data;

@Data
public class ErrorDto {

    private String error;
    private Integer status;
    private String message;
}
