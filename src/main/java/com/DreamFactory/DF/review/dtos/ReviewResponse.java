package com.DreamFactory.DF.review.dtos;

import java.time.LocalDateTime;
import java.util.Date;

public record ReviewResponse(Long id,
                             double rating,
                             String body,
                             LocalDateTime createdAt) {
}
