package com.fig314.cardealers.listing.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Dealer {

    @MongoId
    private String id;

}
