package com.tollwood;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
public class Controller {

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/customers/{userId}/games/recommendations")
    public Collection<Recommendation> getRecommendations(@PathVariable final String userId, @RequestParam(value = "count", defaultValue = "3") final int count) {
        final Collection<Recommendation> result = new ArrayList<Recommendation>();
        for (int i = 0; i < count; i++) {
            result.add(new Recommendation(1, "lotto"));
        }
        return result;
    }
}
