package com.tollwood.integration;

import com.tollwood.Application;
import com.tollwood.dao.RecommendationRepository;
import com.tollwood.model.Recommendation;
import com.tollwood.rest.RecommendationController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@AutoConfigureTestDatabase
public class GameRecommendationTest {

    private static final String USER_ID = "11111";
    private static final Set<String> PRODUCT_RECOMMENDATIONS = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
    private static final MediaType CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    @Autowired
    RecommendationController recommendationController;


    @Autowired
    RecommendationRepository recommendationRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
        final Recommendation recommendation = new Recommendation(USER_ID, true, PRODUCT_RECOMMENDATIONS);
        recommendationRepository.save(recommendation);
    }

    @Test
    public void getRecommendations() throws Exception {
        final int count = 5;
        mockMvc.perform(get("/customers/" + USER_ID + "/games/recommendations?count=" + count))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(count)))
                .andExpect(jsonPath("$[0]", is("A")))
                .andExpect(jsonPath("$[1]", is("B")))
                .andExpect(jsonPath("$[2]", is("C")))
                .andExpect(jsonPath("$[3]", is("D")))
                .andExpect(jsonPath("$[4]", is("E")));
    }
}
