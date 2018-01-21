package com.tollwood.batch;

import com.tollwood.model.CsvInput;
import com.tollwood.model.Recommendation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
@EnableBatchProcessing
public class ImportRecommendationJobFactory {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    public ImportRecommendationJobFactory(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public RecommendationImportItemProcessor processor() {
        return new RecommendationImportItemProcessor();
    }

    @Bean
    public JpaItemWriter<Recommendation> writer() {
        // TODO verify several submits
        JpaItemWriter<Recommendation> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    // TODO verify if its ok to create a new job for each import request
    public Job createJob(byte[] bytes) {
        return jobBuilderFactory.get("importRecommendationJob")
                .incrementer(new RunIdIncrementer())
                .flow(importRecommendation(bytes))
                .end()
                .build();
    }

    private Step importRecommendation(byte[] bytes) {
        return stepBuilderFactory.get("importRecommendation")
                .<CsvInput, Recommendation>chunk(10)
                .reader(reader(bytes))
                .processor(processor())
                .writer(writer())
                .build();
    }

    private FlatFileItemReader<CsvInput> reader(byte[] bytes) {
        FlatFileItemReader<CsvInput> reader = new FlatFileItemReader<>();
        reader.setResource(new ByteArrayResource(bytes));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<CsvInput>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"customerNumber", "recommendationActive", "rec1", "rec2", "rec3", "rec4", "rec5", "rec6", "rec7", "rec8", "rec9", "rec10"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<CsvInput>() {{
                setTargetType(CsvInput.class);
            }});
        }});
        return reader;
    }
}
