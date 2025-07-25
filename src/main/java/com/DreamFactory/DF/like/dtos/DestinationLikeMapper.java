package com.DreamFactory.DF.like.dtos;

import com.DreamFactory.DF.destination.Destination;
import org.springframework.stereotype.Component;

@Component
public class DestinationLikeMapper {
    public DestinationLikeResponse toResponse(Destination destination, boolean liked, long count) {
        return DestinationLikeResponse.builder()
                .destinationId(destination.getId())
                .liked(liked)
                .likeCount(count)
                .build();
    }
}