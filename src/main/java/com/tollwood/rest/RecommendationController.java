package com.tollwood.rest;

import com.tollwood.services.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class RecommendationController {

    public static final int DEFAULT_COUNT = 3;
    @Autowired
    RecommendationService recommendationService;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/customers/{customerNumber}/games/recommendations")
    public Iterable<String> getRecommendations(@PathVariable final String customerNumber, @RequestParam(value = "count", defaultValue = "" + DEFAULT_COUNT) final int count) {
        return recommendationService.getRecommendations(customerNumber, count);
    }

    @PostMapping(value = "/recommendations")
    public void handleFileUpload(@RequestParam("file") MultipartFile file) {
        recommendationService.importRecommendatiomns(file);
    }
}
