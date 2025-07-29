package com.DreamFactory.DF.destination;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.review.Review;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@ActiveProfiles("test")
class DestinationTest {

    @Test
    void testGettersAndSetters() {
        Destination destination = new Destination();
        destination.setId(1L);
        destination.setTitle("Test Title");
        destination.setLocation("Test Location");
        destination.setDescription("Test Description");
        destination.setImageUrl("http://image.url");
        User user = new User();
        destination.setUser(user);
        Review review = new Review();
        destination.setReviews(List.of(review));
        LocalDateTime now = LocalDateTime.now();
        destination.setCreatedAt(now);
        destination.setUpdatedAt(now);

        assertEquals(1L, destination.getId());
        assertEquals("Test Title", destination.getTitle());
        assertEquals("Test Location", destination.getLocation());
        assertEquals("Test Description", destination.getDescription());
        assertEquals("http://image.url", destination.getImageUrl());
        assertEquals(user, destination.getUser());
        assertEquals(1, destination.getReviews().size());
        assertEquals(now, destination.getCreatedAt());
        assertEquals(now, destination.getUpdatedAt());
    }

    @Test
    void testAllArgsConstructorAndBuilder() {
        User user = new User();
        Review review = new Review();
        LocalDateTime now = LocalDateTime.now();
        Destination destination = new Destination(2L, "Title", "Loc", "Desc", "img", user, List.of(review), now, now);
        assertEquals(2L, destination.getId());
        assertEquals("Title", destination.getTitle());
        assertEquals("Loc", destination.getLocation());
        assertEquals("Desc", destination.getDescription());
        assertEquals("img", destination.getImageUrl());
        assertEquals(user, destination.getUser());
        assertEquals(1, destination.getReviews().size());
        assertEquals(now, destination.getCreatedAt());
        assertEquals(now, destination.getUpdatedAt());

        Destination built = Destination.builder()
                .id(3L)
                .title("BTitle")
                .location("BLoc")
                .description("BDesc")
                .imageUrl("Bimg")
                .user(user)
                .reviews(List.of(review))
                .createdAt(now)
                .updatedAt(now)
                .build();
        assertEquals(3L, built.getId());
        assertEquals("BTitle", built.getTitle());
        assertEquals("BLoc", built.getLocation());
        assertEquals("BDesc", built.getDescription());
        assertEquals("Bimg", built.getImageUrl());
        assertEquals(user, built.getUser());
        assertEquals(1, built.getReviews().size());
        assertEquals(now, built.getCreatedAt());
        assertEquals(now, built.getUpdatedAt());
    }

    @Test
    void testOnCreateAndOnUpdate() {
        Destination destination = new Destination();
        destination.onCreate();
        assertNotNull(destination.getCreatedAt());
        assertNotNull(destination.getUpdatedAt());
        LocalDateTime created = destination.getCreatedAt();
        destination.onUpdate();
        assertTrue(destination.getUpdatedAt().isAfter(created) || destination.getUpdatedAt().isEqual(created));
    }

    @Test
    void testEqualsAndHashCodeAndToString() {
        User user = new User();
        Review review = new Review();
        LocalDateTime now = LocalDateTime.now();
        Destination d1 = new Destination(1L, "A", "B", "C", "D", user, List.of(review), now, now);
        Destination d2 = new Destination(1L, "A", "B", "C", "D", user, List.of(review), now, now);
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
        assertNotNull(d1.toString());
    }
}