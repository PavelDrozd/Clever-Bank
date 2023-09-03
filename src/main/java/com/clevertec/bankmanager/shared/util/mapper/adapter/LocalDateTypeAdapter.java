package com.clevertec.bankmanager.shared.util.mapper.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(FORMATTER.format(value));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        String value = in.nextString();
        if (value == null) {
            return null;
        } else {
            return LocalDate.parse(value, FORMATTER);
        }
    }
}