package com.DreamFactory.DF.review.dtos;

import java.time.LocalDateTime;

public record ReviewResponse(Long id,
                             double rating,
                             String body,
                             LocalDateTime createdAt,
                             String username) {
}
