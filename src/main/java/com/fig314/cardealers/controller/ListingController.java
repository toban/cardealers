package com.fig314.cardealers.controller;

import com.fig314.cardealers.listing.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController()
@RequestMapping(value = "/vehicle")
public class ListingController {

    @Autowired
    ListingRepository listingRepository;
    @Autowired
    ListingOperations listingOperations;
    @Autowired
    ListingService listingService;
    @Autowired
    DealerRepository dealerRepository;

    @GetMapping("")
    public Flux<Listing> getVehicleListings() {
        return this.listingRepository.findAll();
    }

    @GetMapping("/dealer/{id}")
    public Flux<Listing> getVehicleDealerListings(@PathVariable("id") String id) {
        return this.listingRepository.findAllByDealer(id);
    }

    @PostMapping("/dealer/{id}")
    public Mono<Void> postVehicleDealerListing(
            @PathVariable("id") String id,
            @RequestBody List<Listing> listings,
            ServerWebExchange swe) {
        return this.dealerRepository.findById(id) // find dealer
                .switchIfEmpty(Mono.error(new Exception("404")))
                .flatMap(dealer -> {
                    for (Listing listing : listings) {
                        listing.setDealer(id);
                    }

                    return // if found
                            this.listingOperations.upsertBulk(listings) // db write
                                    .then(Mono.empty()); // always empty response;
                })
                .onErrorResume(exception -> { // on failure parse exception
                    if (exception.getMessage().equals("404")) {
                        swe.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
                    } else {
                        swe.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    }

                    return Mono.empty();
                })
                .then(Mono.empty());
    }

    @PostMapping("/dealer/{id}/csv")
    public Mono<Void> postVehicleDealerListingCSV(
            @PathVariable("id") String id,
            @RequestBody String listingsCSV,
            ServerWebExchange swe
    ) {
        return
                this.dealerRepository.findById(id) // find dealer
                        .switchIfEmpty(Mono.error(new Exception("404")))
                        .then(ListingReads.readCSVList(listingsCSV).collectList()) // try parse csv
                        .flatMap(listings -> { // db write
                            for (Listing listing : listings) {
                                listing.setDealer(id);
                            }

                            return this.listingOperations.upsertBulk(listings)
                                    .then(Mono.empty());
                        })
                        .onErrorResume(exception -> { // on failure parse exception
                            if (exception.getMessage().equals("404")) {
                                swe.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
                            } else {
                                swe.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                            }

                            return Mono.empty();
                        })
                        .then(Mono.empty()); // always empty response

    }

    @GetMapping("/search")
    public Flux<Listing> getSearchListings(ServerWebExchange swe) {

        return this.listingService.searchBy(swe.getRequest().getQueryParams().toSingleValueMap());
    }

}
