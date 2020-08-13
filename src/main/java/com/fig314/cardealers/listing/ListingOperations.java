package com.fig314.cardealers.listing;

import com.mongodb.client.model.FindOneAndReplaceOptions;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.ReactiveRemoveOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.Doc;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ListingOperations {

    @Autowired
    ReactiveMongoTemplate template;

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