package com.DreamFactory.DF.destination.dto;

import com.DreamFactory.DF.review.dtos.ReviewResponse;

import java.time.LocalDateTime;
import java.util.List;

public record DestinationWithReviewsResponse(
                Long id,
                String title,
                String location,
                String description,
                String imageUrl,
                String username,
                List<ReviewResponse> reviews,
                Double rating,
                LocalDateTime createdAt,
                LocalDateTime updatedAt) {
}