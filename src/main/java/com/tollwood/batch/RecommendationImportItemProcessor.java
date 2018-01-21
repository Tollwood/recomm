package com.tollwood.batch;

import com.tollwood.model.CsvInput;
import com.tollwood.model.Recommendation;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashSet;
import java.util.Set;

public class RecommendationImportItemProcessor implements ItemProcessor<CsvInput, Recommendation> {
    @Override
    public Recommendation process(CsvInput csvInput) {
        Set<String> products = new HashSet<>();
        products.add(csvInput.getRec1());
        products.add(csvInput.getRec2());
        products.add(csvInput.getRec3());
        products.add(csvInput.getRec4());
        products.add(csvInput.getRec5());
        products.add(csvInput.getRec6());
        products.add(csvInput.getRec7());
        products.add(csvInput.getRec8());
        products.add(csvInput.getRec9());
        products.add(csvInput.getRec10());
        return new Recommendation(csvInput.getCustomerNumber(), csvInput.isRecommendationActive(), products);
    }
}
