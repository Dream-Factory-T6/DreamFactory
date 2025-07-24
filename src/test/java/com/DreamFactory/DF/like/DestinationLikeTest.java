package com.DreamFactory.DF.like;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class DestinationLikeTest {
    private User user;
    private Destination destination;
    private DestinationLike like;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        destination = new Destination();
        like = new DestinationLike();
        like.setId(10L);
        like.setUser(user);
        like.setDestination(destination);
    }

    @Test
    void builder_ShouldBuildObjectCorrectly() {
        assertEquals(10L, like.getId());
        assertEquals(user, like.getUser());
        assertEquals(destination, like.getDestination());
        assertNull(like.getCreatedAt());
    }

    @Test
    void onCreate_ShouldSetCreatedAt() {
        assertNull(like.getCreatedAt());

        like.onCreate();

        assertNotNull(like.getCreatedAt());
        assertTrue(like.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}