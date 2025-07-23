package com.DreamFactory.DF.review.dtos;
import com.DreamFactory.DF.review.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public static Review toEntity(ReviewRequest request){
        return Review.builder()
                .rating(request.rating())
                .body(request.body())
                .build();
    }

    public static ReviewResponse toReviewResponse(Review review){
        return new ReviewResponse(review.getId(),
                review.getRating(),
                review.getBody(),
                review.getCreatedAt(),
                review.getUser().getUsername());
    }
}
