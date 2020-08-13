package com.fig314.cardealers.listing;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ListingRepository extends ReactiveMongoRepository<Listing, String> {
    @Query("{'properties.vendor': ?0}")
    Flux<Listing> findByVendorID(UUID vendorUUID);
}