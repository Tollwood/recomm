package com.tollwood.services;

import com.tollwood.batch.ImportRecommendationLauncher;
import com.tollwood.dao.RecommendationRepository;
import com.tollwood.model.Recommendation;
import com.tollwood.rest.ImportFailedException;
import com.tollwood.rest.NoRecommendationFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashSet;

import static java.util.stream.Collectors.toCollection;

@Component
public class RecommendationService {

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    ImportRecommendationLauncher importRecommendationLauncher;

    public Iterable<String> getRecommendations(String customerNumber, int count) {
        final Recommendation recommendation = recommendationRepository.findByCustomerNumber(customerNumber);
        if (recommendation == null || !recommendation.isRecommendationActive()) {
            throw new NoRecommendationFoundException();
        }
        return recommendation.getProductRecommendations().stream()
                .limit(count)
                .collect(toCollection(LinkedHashSet::new));
    }

    public void importRecommendatiomns(MultipartFile file) {
        try {
            importRecommendationLauncher.run(file.getBytes());
        } catch (IOException e) {
            throw new ImportFailedException();
        }
    }
}
