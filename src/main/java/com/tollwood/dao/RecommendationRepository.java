package com.tollwood.dao;

import com.tollwood.model.Recommendation;
import org.springframework.data.repository.CrudRepository;

public interface RecommendationRepository extends CrudRepository<Recommendation, Long> {
    Recommendation findByCustomerNumber(String customerNumber);
}
