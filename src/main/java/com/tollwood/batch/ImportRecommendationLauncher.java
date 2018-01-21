package com.tollwood.batch;

import com.tollwood.rest.ImportFailedException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ImportRecommendationLauncher {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ImportRecommendationJobFactory importRecommendationJobFactory;

    public void run(byte[] bytes) {
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        final Job job = importRecommendationJobFactory.createJob(bytes);
        try {
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            throw new ImportFailedException();
        }
    }
}
