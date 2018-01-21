package com.tollwood.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String customerNumber;
    private boolean recommendationActive;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_recommendations", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "productRecommendations")
    private Set<String> productRecommendations;

    protected Recommendation() {
    }

    public Recommendation(String customerNumber, boolean recommendationActive, Set<String> productRecommendations) {
        this.customerNumber = customerNumber;
        this.recommendationActive = recommendationActive;
        this.productRecommendations = productRecommendations;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public boolean isRecommendationActive() {
        return recommendationActive;
    }

    public Set<String> getProductRecommendations() {
        return productRecommendations;
    }
}
