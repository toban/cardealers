package com.fig314.cardealers.listing.exception;

public class ParsingFailed extends Exception {
    public ParsingFailed(Exception outer) {
        super(outer);
    }
}
