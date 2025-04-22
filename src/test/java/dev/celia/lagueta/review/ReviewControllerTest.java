package dev.celia.lagueta.review;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class ReviewControllerTest {

    private ReviewService reviewService;
    private ReviewController reviewController;

    private Review sampleReview;

    @BeforeEach
    void setUp() {
        reviewService = mock(ReviewService.class);
        reviewController = new ReviewController(reviewService);

        sampleReview = new Review();
        sampleReview.setId(1L);
        sampleReview.setAuthor("John Doe");
        sampleReview.setContent("Buen bar!");
        sampleReview.setRating(5);
    }

    @Test
    void testGetAllReviews() {
        when(reviewService.findAll()).thenReturn(List.of(sampleReview));

        List<Review> reviews = reviewController.getAllReviews();

        assertEquals(1, reviews.size());
        assertEquals("John Doe", reviews.get(0).getAuthor());
    }

    @Test
    void testGetReviewById_found() {
        when(reviewService.findById(1L)).thenReturn(Optional.of(sampleReview));

        ResponseEntity<Review> response = reviewController.getReviewById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getAuthor());
    }

    @Test
    void testGetReviewById_notFound() {
        when(reviewService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Review> response = reviewController.getReviewById(99L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testCreateReview() {
        when(reviewService.save(sampleReview)).thenReturn(sampleReview);

        ResponseEntity<Review> response = reviewController.createReview(sampleReview);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Doe", response.getBody().getAuthor());
        verify(reviewService).save(sampleReview);
    }

    @Test
    void testDeleteReview_found() {
        when(reviewService.findById(1L)).thenReturn(Optional.of(sampleReview));
        doNothing().when(reviewService).deleteById(1L);

        ResponseEntity<Void> response = reviewController.deleteReview(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(reviewService).deleteById(1L);
    }

    @Test
    void testDeleteReview_notFound() {
        when(reviewService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = reviewController.deleteReview(99L);

        assertEquals(404, response.getStatusCode().value());
        verify(reviewService, never()).deleteById(any());
    }
}
