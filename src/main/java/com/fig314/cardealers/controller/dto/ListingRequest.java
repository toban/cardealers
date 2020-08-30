package com.fig314.cardealers.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ListingRequest {
    private String code;
    private String make;
    private String model;
    private Integer kw;
    private Integer year;
    private String color;
    private BigDecimal price;
}
