package com.fig314.cardealers.listing;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DealerRepository extends ReactiveMongoRepository<Dealer, String> {

}