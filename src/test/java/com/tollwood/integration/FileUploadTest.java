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
import java.util.*;

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

    // @Test
    // TODO Transactions for tests are not rolled back
    public void shouldImportUploadedFile() throws Exception {
        // given
        final Set<String> recommendetProducts1 = new HashSet<>(Arrays.asList("bingo", "cashwheel", "cashbuster", "brilliant", "citytrio", "crossword", "sevenwins", "sudoku", "sofortlotto", "hattrick"));
        final Recommendation recommendation1 = new Recommendation("111111", true, recommendetProducts1);

        final Set<String> recommendetProducts2 = new HashSet<>(Arrays.asList("brilliant", "citytrio", "crossword", "sevenwins", "sudoku", "sofortlotto", "hattrick", "bingo", "cashwheel", "cashbuster"));
        final Recommendation recommendation2 = new Recommendation("111112", false, recommendetProducts2);

        final Set<String> recommendetProducts3 = new HashSet<>(Arrays.asList("brilliant", "citytrio", "crossword", "sevenwins", "sudoku", "sofortlotto", "hattrick", "bingo", "cashwheel", "cashbuster"));
        final Recommendation recommendation3 = new Recommendation("111113", false, recommendetProducts3);

        // when
        final String originalFilename = "input.csv";
        uploadFile(originalFilename);

        // then
        final Iterable<Recommendation> allRecommendations = recommendationRepository.findAll();
        List<Recommendation> list = new ArrayList<>();
        allRecommendations.forEach(list::add);
        assertEquals(3, allRecommendations.spliterator().getExactSizeIfKnown());
        assertEquals(recommendation1, list.get(0));
        assertEquals(recommendation2, list.get(1));
        assertEquals(recommendation3, list.get(2));
    }


    @Test
    public void shouldUpdateExistingRecommendationsWithSecondImport() throws Exception {
        // given
        final Set<String> recommendetProducts1 = new HashSet<>(Arrays.asList("B", "R", "I", "L", "L", "I", "A", "N", "T", "S"));
        final Recommendation recommendation1 = new Recommendation("111111", false, recommendetProducts1);

        final Set<String> recommendetProducts2 = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
        final Recommendation recommendation2 = new Recommendation("111112", true, recommendetProducts2);

        final Set<String> recommendetProducts3 = new HashSet<>(Arrays.asList("J", "I", "H", "S", "A", "F", "X", "Y", "R", "M"));
        final Recommendation recommendation3 = new Recommendation("111113", true, recommendetProducts3);

        final Set<String> recommendetProducts4 = new HashSet<>(Arrays.asList("I", "T", "W", "O", "R", "K", "S", "O", "K", "!"));
        final Recommendation recommendation4 = new Recommendation("111114", false, recommendetProducts4);

        // when
        final String firstFile = "input.csv";
        uploadFile(firstFile);

        final String secondFile = "input-2.csv";
        uploadFile(secondFile);

        // then
        final Iterable<Recommendation> allRecommendationsAfterSecondFile = recommendationRepository.findAll();
        List<Recommendation> list = new ArrayList<>();
        allRecommendationsAfterSecondFile.forEach(list::add);
        assertEquals(4, list.size());
        assertEquals(recommendation1, list.get(0));
        assertEquals(recommendation2, list.get(1));
        assertEquals(recommendation3, list.get(2));
        assertEquals(recommendation4, list.get(3));
    }

    private void uploadFile(final String originalFilename) throws Exception {

        final InputStream inputStream = this.getClass().getResourceAsStream("/" + originalFilename);

        MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename,
                "text/plain", inputStream);

        mockMvc.perform(fileUpload("/recommendations").file(multipartFile))
                .andExpect(status().isOk());
    }

}
