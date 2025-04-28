package dev.celia.lagueta.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class ReviewTest {
    @Test
    void testGettersandSetters() {
        Review review = new Review();

        review.setId(1L);
        review.setAuthor("John Doe");
        review.setContent("Buen bar!");
        review.setRating(5);
        LocalDateTime now = LocalDateTime.now();
        review.setCreatedAt(now);

        assertEquals(1L, review.getId());
        assertEquals("John Doe", review.getAuthor());
        assertEquals("Buen bar!", review.getContent());
        assertEquals(5, review.getRating());
        assertEquals(now, review.getCreatedAt());

    }

    @Test
    void testPrePersistSetsCreatedAt() {
        Review review = new Review();
        review.prePersist();

        assertNotNull(review.getCreatedAt());
        assertTrue(review.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

}
