package com.fig314.cardealers.listing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(value = "/vehicle")
public class ListingController {

    @Autowired
    ListingRepository listingRepository;
    @Autowired
    ListingOperations listingOperations;

    @GetMapping("/")
    public Flux<Listing> getVehicleListings() {
        return this.listingRepository.findAll();
    }

    @GetMapping("/vendor/{id}/")
    public Flux<Listing> getVehicleVendorListings(@PathVariable("uuid") UUID uuid) {
        return this.listingRepository.findByVendorID(uuid);
    }

    @PostMapping("/vendor/{id}/")
    public Flux<Listing> postVehicleVendorListing(@PathVariable("id") String id, @RequestBody List<Listing> listings) {
        List<Mono<Listing>> upserts = new ArrayList<>();

        for (Listing listing: listings) {
            listing.setDealer(id);

            upserts.add(this.listingOperations.upsert(listing));
        }

        return Flux.merge(upserts);
    }

    @PostMapping("/vendor/{id}/csv")
    public Flux<Listing> postVehicleVendorListingCSV(@PathVariable("id") String id, @RequestBody String listingsCSV) {
        List<Mono<Listing>> upserts = new ArrayList<>();

        for (Listing listing: listings) {
            listing.setDealer(id);

            upserts.add(this.listingOperations.upsert(listing));
        }

        return Flux.merge(upserts);
    }

}
