package com.fig314.cardealers.listing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListingOperations {

    @Autowired
    ReactiveMongoTemplate template;

    @Transactional
    public Flux<Listing> upsertBulk(List<Listing> listings) {
        List<Mono<Listing>> upserts = new ArrayList<>();

        for (Listing listing : listings) {
            upserts.add(this.upsert(listing));
        }

        return Flux.merge(upserts);
    }

    public Mono<Listing> upsert(Listing listing) {
        Query query = new Query(Criteria.where("code").is(listing.getCode()));
        query.addCriteria(Criteria.where("dealer").is(listing.getDealer()));

        FindAndReplaceOptions findAndReplaceOptions = new FindAndReplaceOptions();
        findAndReplaceOptions.upsert();
        findAndReplaceOptions.returnNew();

        return template.findAndReplace(
                query,
                listing,
                findAndReplaceOptions
        );
    }
}