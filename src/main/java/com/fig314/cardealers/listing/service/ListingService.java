package com.fig314.cardealers.listing.service;

import com.fig314.cardealers.listing.data.Listing;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public interface ListingService {
    public Mono<Void> processSaveRequest(String dealerID, List<Listing> listingList);

    public Flux<Listing> searchBy(Map<String, String> params);

    public Flux<Listing> getVehicleListingsByDealer(String dealerID);

    public Flux<Listing> getVehicleListings();
}
