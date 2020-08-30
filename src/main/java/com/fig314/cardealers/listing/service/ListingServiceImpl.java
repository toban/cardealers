package com.fig314.cardealers.listing.service;

import com.fig314.cardealers.listing.data.Listing;
import com.fig314.cardealers.listing.exception.EntityNotFound;
import com.fig314.cardealers.listing.repository.DealerRepository;
import com.fig314.cardealers.listing.repository.ListingOperations;
import com.fig314.cardealers.listing.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ListingServiceImpl implements ListingService {
    @Autowired
    ListingRepository listingRepository;
    @Autowired
    DealerRepository dealerRepository;
    @Autowired
    ListingOperations listingOperations;

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

    public Mono<Void> processSaveRequest(String dealerID, List<Listing> listingList) {
        return this.dealerRepository.findById(dealerID)
                .switchIfEmpty(Mono.error(new EntityNotFound("Dealer")))
                .flatMap(dealer -> {
                    return // if found
                            this.listingOperations.upsertBulk(listingList) // db write
                                    .then(Mono.empty()); // once finished return nothing
                });
    }

    public Flux<Listing> getVehicleListingsByDealer(String dealerID) {
        return this.listingRepository.findAllByDealer(dealerID);
    }

    public Flux<Listing> getVehicleListings() {
        return this.listingRepository.findAll();
    }
}
