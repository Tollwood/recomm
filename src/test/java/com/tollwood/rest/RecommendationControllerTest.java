package com.tollwood.rest;

import com.tollwood.Application;
import com.tollwood.services.RecommendationService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@AutoConfigureTestDatabase
public class RecommendationControllerTest {

    private static final String USER_ID = "11111";
    private static final MediaType CONTENT_TYPE = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    @Autowired
    private RecommendationController recommendationController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(recommendationController).build();
    }

    @Test
    public void getRecommendationsWithNoRecommendationFound() throws Exception {
        Mockito.when(recommendationService.getRecommendations(USER_ID, 3)).thenThrow(NoRecommendationFoundException.class);
        mockMvc.perform(get("/customers/" + USER_ID + "/games/recommendations"))
                .andExpect(status().isNotFound());
        verify(recommendationService).getRecommendations(USER_ID, 3);
    }

    @Test
    public void getRecommendationsWithDefault() throws Exception {
        Iterable<String> recommendations = Lists.newArrayList("A", "B", "C");

        when(recommendationService.getRecommendations(USER_ID, RecommendationController.DEFAULT_COUNT)).thenReturn(recommendations);

        mockMvc.perform(get("/customers/" + USER_ID + "/games/recommendations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(RecommendationController.DEFAULT_COUNT)))
                .andExpect(jsonPath("$[0]", is("A")))
                .andExpect(jsonPath("$[1]", is("B")))
                .andExpect(jsonPath("$[2]", is("C")));
        verify(recommendationService).getRecommendations(USER_ID, RecommendationController.DEFAULT_COUNT);
    }

    @Test
    public void getRecommendations() throws Exception {
        final int count = 5;
        Iterable<String> recommendations = Lists.newArrayList("A", "B", "C", "D", "E");

        when(recommendationService.getRecommendations(USER_ID, count)).thenReturn(recommendations);

        mockMvc.perform(get("/customers/" + USER_ID + "/games/recommendations?count=" + count))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(count)))
                .andExpect(jsonPath("$[0]", is("A")))
                .andExpect(jsonPath("$[1]", is("B")))
                .andExpect(jsonPath("$[2]", is("C")))
                .andExpect(jsonPath("$[3]", is("D")))
                .andExpect(jsonPath("$[4]", is("E")));
        verify(recommendationService).getRecommendations(USER_ID, count);
    }

    @Test
    public void shouldImportUploadedFile() throws Exception {
        final InputStream inputStream = this.getClass().getResourceAsStream("/input.csv");
        final String originalFilename = "input.csv";
        MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename,
                "text/plain", inputStream);

        mockMvc.perform(fileUpload("/recommendations").file(multipartFile))
                .andExpect(status().isOk());
    }

}