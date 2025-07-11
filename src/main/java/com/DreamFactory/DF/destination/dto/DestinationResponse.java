package com.DreamFactory.DF.destination.dto;

import java.time.LocalDateTime;

public record DestinationResponse(
        Long id,
        String title,
        String location,
        String description,
        String imageUrl,
        String username,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
