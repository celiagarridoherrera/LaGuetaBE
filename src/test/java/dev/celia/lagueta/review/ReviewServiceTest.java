package dev.celia.lagueta.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private ReviewService reviewService;
    private Review sampleReview;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        sampleReview = new Review();
        sampleReview.setId(1L);
        sampleReview.setAuthor("John Doe");
        sampleReview.setContent("Buen bar!");
        sampleReview.setRating(5);
    }

    @Test
    void testDeleteById() {
        reviewService.deleteById(1L);
        verify((reviewRepository)).deleteById(1L);

    }

    @Test
    void testFindAll() {
        when(reviewRepository.findAll()).thenReturn(Collections.singletonList(sampleReview));

        List<Review> reviews = reviewService.findAll();

        assertEquals(1, reviews.size());
        assertEquals("John Doe", reviews.get(0).getAuthor());
        assertEquals("Buen bar!", reviews.get(0).getContent());

    }

    @Test
    void testFindById() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));
        
        Optional<Review> found = reviewService.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getAuthor());
    }

    @Test
    void testSave() {
        when(reviewRepository.save(sampleReview)).thenReturn(sampleReview);
        
        Review saved = reviewService.save(sampleReview);
        assertNotNull(saved);
        assertEquals("Buen bar!", saved.getContent());

    }
}
