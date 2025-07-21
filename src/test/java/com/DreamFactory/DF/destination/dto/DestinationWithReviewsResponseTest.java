package com.DreamFactory.DF.destination.dto;

import com.DreamFactory.DF.review.dtos.ReviewResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

class DestinationWithReviewsResponseTest {

    @Test
    void testAccessors() {
        LocalDateTime now = LocalDateTime.now();
        ReviewResponse review = new ReviewResponse(1L, 5.0, "text", now, "user");
        DestinationWithReviewsResponse resp = new DestinationWithReviewsResponse(
                1L, "title", "loc", "desc", "img", "user", now, now, List.of(review));
        assertEquals(1L, resp.id());
        assertEquals("title", resp.title());
        assertEquals("loc", resp.location());
        assertEquals("desc", resp.description());
        assertEquals("img", resp.imageUrl());
        assertEquals("user", resp.username());
        assertEquals(now, resp.createdAt());
        assertEquals(now, resp.updatedAt());
        assertEquals(1, resp.reviews().size());
        assertEquals(review, resp.reviews().get(0));
    }
}