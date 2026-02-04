package br.pietroth.modularweather.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleJsonParser<T> {
    private final ObjectMapper objectMapper;
    private final Class<T> type;

    public SimpleJsonParser(Class<T> type) {
        this.objectMapper = new ObjectMapper();
        this.type = type;
    }

    public T parse(String json) {
        try {
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}

