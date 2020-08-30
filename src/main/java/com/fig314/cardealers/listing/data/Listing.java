package com.fig314.cardealers.listing.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;

@Document
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Listing {

    @MongoId
    private String id;
    private String dealer;
    private String code;
    private String make;
    private String model;
    private Integer kw;
    private Integer year;
    private String color;
    private BigDecimal price;

}