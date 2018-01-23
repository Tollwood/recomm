package com.tollwood.services;

import com.tollwood.dao.RecommendationRepository;
import com.tollwood.model.Recommendation;
import com.tollwood.rest.NoRecommendationFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
public class RecommendationServiceTest {

    private static final String USER_ID = "11111";
    private static final Set<String> PRODUCT_RECOMMENDATIONS = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"));
    @InjectMocks
    @Autowired
    RecommendationService recommendationService;
    @Mock
    private RecommendationRepository recommendationRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = NoRecommendationFoundException.class)
    public void getRecommendationsWithNoRecommendationFound() {
        when(recommendationRepository.findByCustomerNumber(USER_ID)).thenReturn(null);
        recommendationService.getRecommendations(USER_ID, 3);
        verify(recommendationRepository).findByCustomerNumber(USER_ID);
    }

    @Test(expected = NoRecommendationFoundException.class)
    public void getRecommendations404IfRecommendationNotActive() {
        when(recommendationRepository.findByCustomerNumber(USER_ID)).thenReturn(new Recommendation(USER_ID, false, null));
        recommendationService.getRecommendations(USER_ID, 3);
        verify(recommendationRepository).findByCustomerNumber(USER_ID);
    }

    @Test
    public void getRecommendationsWithCount() {
        final int count = 5;
        when(recommendationRepository.findByCustomerNumber(USER_ID)).thenReturn(new Recommendation(USER_ID, true, PRODUCT_RECOMMENDATIONS));
        final Iterable<String> actual = recommendationService.getRecommendations(USER_ID, count);
        verify(recommendationRepository).findByCustomerNumber(USER_ID);
        assertEquals(count, actual.spliterator().getExactSizeIfKnown());
    }
}