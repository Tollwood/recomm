package com.tollwood.services;

import com.tollwood.dao.RecommendationRepository;
import com.tollwood.model.Recommendation;
import com.tollwood.rest.ImportFailedException;
import com.tollwood.rest.NoRecommendationFoundException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashSet;

import static java.util.stream.Collectors.toCollection;

@Component
public class RecommendationService {

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importRecommendationJob")
    Job importRecommendationJob;

    public Iterable<String> getRecommendations(String customerNumber, int count) {
        final Recommendation recommendation = recommendationRepository.findByCustomerNumber(customerNumber);
        if (recommendation == null || !recommendation.isRecommendationActive()) {
            throw new NoRecommendationFoundException(customerNumber);
        }
        return recommendation.getProductRecommendations().stream()
                .limit(count)
                .collect(toCollection(LinkedHashSet::new));
    }

    public void importRecommendatiomns(MultipartFile file) {
        try {
            final File tmpFile = convertMultipartFileToFile(file);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("pathToFile", tmpFile.getAbsolutePath())
                    .toJobParameters();
            jobLauncher.run(importRecommendationJob, jobParameters);
        } catch (Exception e) {
            throw new ImportFailedException(e.getMessage());
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = File.createTempFile("csv-import", ".csv");
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
