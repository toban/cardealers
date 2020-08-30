package com.fig314.cardealers.listing.repository;

import com.fig314.cardealers.listing.data.Dealer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DealerRepository extends ReactiveMongoRepository<Dealer, String> {

}