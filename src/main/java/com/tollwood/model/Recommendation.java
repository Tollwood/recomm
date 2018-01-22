package com.tollwood.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@EqualsAndHashCode
@ToString
@Getter
public class Recommendation {

    @Id
    private final String customerNumber;
    private final boolean recommendationActive;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_recommendations", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "productRecommendations")
    private final Set<String> productRecommendations;

    protected Recommendation() {
        this(null, false, null);
    }

    public Recommendation(String customerNumber, boolean recommendationActive, Set<String> productRecommendations) {
        this.customerNumber = customerNumber;
        this.recommendationActive = recommendationActive;
        this.productRecommendations = productRecommendations;
    }
}
