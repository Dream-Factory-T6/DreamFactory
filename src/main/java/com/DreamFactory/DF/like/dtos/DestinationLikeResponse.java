package com.DreamFactory.DF.like.dtos;

import lombok.Builder;

@Builder
public record DestinationLikeResponse(
        Long destinationId,
        boolean liked,
        long likeCount
) {}