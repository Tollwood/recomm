package com.tollwood.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoRecommendationFoundException extends RuntimeException {
    public NoRecommendationFoundException(String customerNumber) {
        super("No recommendation found for " + customerNumber);
    }
}
