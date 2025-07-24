package com.DreamFactory.DF.like.dtos;

import com.DreamFactory.DF.destination.Destination;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DestinationLikeMapperTest {
    private final DestinationLikeMapper mapper = new DestinationLikeMapper();

    @Test
    void toResponse_ShouldMapFieldsCorrectly() {
        Destination destination = new Destination();
        destination.setId(100L);
        boolean liked = true;
        long count = 1L;

        DestinationLikeResponse response = mapper.toResponse(destination, liked, count);

        assertEquals(100L, response.destinationId());
        assertTrue(response.liked());
        assertEquals(1L, response.likeCount());
    }
}