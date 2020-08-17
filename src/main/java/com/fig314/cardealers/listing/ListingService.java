package com.fig314.cardealers.listing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class ListingService {
    @Autowired
    ListingRepository listingRepository;

    public Flux<Listing> searchBy(Map<String, String> params) {
        String model = params.get("model");
        String make = params.get("make");
        Integer year = null;
        String color = params.get("color");

        try {
            year = Integer.parseInt(params.get("year"));
        } catch (Exception e) {
        }

        Listing example = new Listing();

        example.setModel(model);
        example.setMake(make);
        example.setYear(year);
        example.setColor(color);

        return this.listingRepository.findAll(Example.of(example, ExampleMatcher.matching()));
    }
}
