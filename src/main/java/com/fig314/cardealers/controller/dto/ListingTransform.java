package com.fig314.cardealers.controller.dto;

import com.fig314.cardealers.listing.data.Listing;
import com.fig314.cardealers.listing.parsing.ListingCSVRow;

import java.util.List;
import java.util.stream.Collectors;

public class ListingTransform {
    static public Listing listingFromRequest(ListingRequest listingRequest) {
        return new Listing(
                null,
                null,
                listingRequest.getCode(),
                listingRequest.getMake(),
                listingRequest.getModel(),
                listingRequest.getKw(),
                listingRequest.getYear(),
                listingRequest.getColor(),
                listingRequest.getPrice()
        );
    }

    static public Listing listingFromCSVRow(ListingCSVRow listingCSVRow) {
        return new Listing(
                null,
                null,
                listingCSVRow.getCode(),
                listingCSVRow.getMake(),
                listingCSVRow.getModel(),
                listingCSVRow.getKw(),
                listingCSVRow.getYear(),
                listingCSVRow.getColor(),
                listingCSVRow.getPrice()
        );
    }

    static public ListingResponse responseFromListing(Listing listing) {
        return new ListingResponse(
                listing.getDealer(),
                listing.getCode(),
                listing.getMake(),
                listing.getModel(),
                listing.getKw(),
                listing.getYear(),
                listing.getColor(),
                listing.getPrice()
        );
    }

    static public List<Listing> withDealerID(List<ListingRequest> listingRequestList, String dealerID) {
        return listingRequestList
                .stream()
                .map(ListingTransform::listingFromRequest)
                .map(listing -> {
                    listing.setDealer(dealerID);
                    return listing;
                })
                .collect(Collectors.toList());
    }

    static public List<Listing> withDealerIDFromCSVRow(List<ListingCSVRow> listingRequestList, String dealerID) {
        return listingRequestList
                .stream()
                .map(ListingTransform::listingFromCSVRow)
                .map(listing -> {
                    listing.setDealer(dealerID);
                    return listing;
                })
                .collect(Collectors.toList());
    }
}
