package com.fig314.cardealers.listing;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.util.UUID;

@Document
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Listing {

    @Id
    private String id;
    private String dealer;
    private String code;
    private String make;
    private String model;
    private Integer kW;
    private Integer year;
    private String color;
    private BigDecimal price;

}