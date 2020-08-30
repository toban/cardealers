package com.fig314.cardealers.listing.exception;

import lombok.Getter;

public class EntityNotFound extends Exception {
    @Getter
    private String entity;

    public EntityNotFound(String entity) {
        super(entity + " not found.");
    }
}
