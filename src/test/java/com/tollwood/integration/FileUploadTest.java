package com.tollwood.integration;

import com.tollwood.Application;
import com.tollwood.dao.RecommendationRepository;
import com.tollwood.model.Recommendation;
import com.tollwood.rest.RecommendationController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class FileUploadTest {


    @Autowired
    RecommendationController recommendationController;

    @Autowired
    RecommendationRepository recommendationRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
    }

    @Test
    public void shouldImportUploadedFile() throws Exception {
        final String originalFilename = "input.csv";
        final InputStream inputStream = this.getClass().getResourceAsStream("/" + originalFilename);

        MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename,
                "text/plain", inputStream);

        mockMvc.perform(fileUpload("/recommendations").file(multipartFile))
                .andExpect(status().isOk());

        final Iterable<Recommendation> allRecommendations = recommendationRepository.findAll();
        assertEquals(3, allRecommendations.spliterator().getExactSizeIfKnown());

        // TODO verify eachh row
    }

}
