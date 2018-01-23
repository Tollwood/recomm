package com.tollwood.batch;

import com.tollwood.model.CsvInput;
import com.tollwood.model.Recommendation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
@EnableBatchProcessing
public class ImportRecommendationFromCsvBatchConfiguration {

    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    public ImportRecommendationFromCsvBatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean(name = "importRecommendationJob")
    public Job importRecommendationJob(Step step) {
        return jobBuilderFactory.get("importRecommendationJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step importRecommendation(FlatFileItemReader<CsvInput> reader) {
        return stepBuilderFactory.get("importRecommendation")
                .<CsvInput, Recommendation>chunk(10)
                .reader(reader)
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CsvInput> reader(@Value("#{jobParameters[pathToFile]}") String pathToFile) {

        FlatFileItemReader<CsvInput> reader = new FlatFileItemReader<>();
        // desparate on this one.... exception on startup as pathToFile is not defined when loading the configuration
        if (pathToFile != null) {
            reader.setResource(new FileSystemResource(pathToFile));
        } else {
            reader.setResource(new ByteArrayResource("".getBytes()));
        }
        reader.setLinesToSkip(1);
        reader.setStrict(false);
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

    @Bean
    public RecommendationImportItemProcessor processor() {
        return new RecommendationImportItemProcessor();
    }

    @Bean
    public JpaItemWriter<Recommendation> writer() {
        JpaItemWriter<Recommendation> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }
}
