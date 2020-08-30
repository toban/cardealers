package com.fig314.cardealers.controller;

import com.fig314.cardealers.controller.dto.ListingRequest;
import com.fig314.cardealers.controller.dto.ListingResponse;
import com.fig314.cardealers.controller.dto.ListingTransform;
import com.fig314.cardealers.listing.service.ListingService;
import com.fig314.cardealers.listing.exception.EntityNotFound;
import com.fig314.cardealers.listing.exception.ParsingFailed;
import com.fig314.cardealers.listing.parsing.ListingReads;
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
    ListingService listingService;

    @GetMapping("")
    public Flux<ListingResponse> getVehicleListings() {
        return listingService.getVehicleListings().map(ListingTransform::responseFromListing);
    }

    @GetMapping("/dealer/{id}")
    public Flux<ListingResponse> getVehicleListingsByDealer(@PathVariable("id") String dealerID) {
        return listingService.getVehicleListingsByDealer(dealerID).map(ListingTransform::responseFromListing);
    }

    @PostMapping("/dealer/{id}")
    public Mono<Void> postVehicleDealerListing(
            @PathVariable("id") String dealerID,
            @RequestBody List<ListingRequest> listings,
            ServerWebExchange swe) {
        return this.listingService.processSaveRequest(dealerID, ListingTransform.withDealerID(listings, dealerID))
                .onErrorResume(exception -> { // on failure parse exception
                    if (exception instanceof EntityNotFound) {
                        swe.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
                    } else {
                        swe.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    return Mono.empty();
                });
    }

    @PostMapping("/dealer/{id}/csv")
    public Mono<Void> postVehicleDealerListingCSV(
            @PathVariable("id") String dealerID,
            @RequestBody String listingsCSV,
            ServerWebExchange swe
    ) {
        return
                ListingReads.readCSVList(listingsCSV).collectList()
                        .flatMap(listings -> listingService.processSaveRequest(dealerID, ListingTransform.withDealerIDFromCSVRow(listings, dealerID)))
                        .onErrorResume(exception -> {
                            if (exception instanceof EntityNotFound) {
                                swe.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
                            } else if (exception instanceof ParsingFailed) {
                                swe.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                            } else {
                                swe.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                            }

                            return Mono.empty();
                        });
    }

    @GetMapping("/search")
    public Flux<ListingResponse> getSearchListings(ServerWebExchange swe) {
        return this.listingService.searchBy(swe.getRequest().getQueryParams().toSingleValueMap()).map(ListingTransform::responseFromListing);
    }

}
