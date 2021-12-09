package com.demo.relationalGeode;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
public class CustomFirstObject {

    private Integer id;
    private Double amount;

    public @NonNull CustomFirstObject identifiedAs(Integer id) {
        setId(id);
        return this;
    }

    public @NonNull CustomFirstObject withAmount(Double amount) {
        setAmount(amount);
        return this;
    }
}
