package com.clevertec.bankmanager.shared.util.mapper;

import com.clevertec.bankmanager.shared.util.mapper.adapter.LocalDateTimeTypeAdapter;
import com.clevertec.bankmanager.shared.util.mapper.adapter.LocalDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

public enum GsonMapper {
    INSTANCE;
    private final Gson gson;

    GsonMapper() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
    }

    public Gson getInstance() {
        return gson;
    }
}
