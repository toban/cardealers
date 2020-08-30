package com.fig314.cardealers.listing.repository;

import com.fig314.cardealers.listing.data.Listing;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ListingRepository extends ReactiveMongoRepository<Listing, String> {
    @Query(value = "{'dealer': ?0}")
    Flux<Listing> findAllByDealer(String dealer);
}