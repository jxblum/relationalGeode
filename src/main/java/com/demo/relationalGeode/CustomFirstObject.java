package com.demo.relationalGeode;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomFirstObject implements Serializable {
    private Integer id;
    private Double amount;
}
