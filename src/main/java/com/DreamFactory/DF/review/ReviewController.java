package com.DreamFactory.DF.review;


import com.DreamFactory.DF.review.dtos.ReviewResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review", description = "Operations related to review")
public class ReviewController {
    private  final ReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<List<ReviewResponse>> showReviewsByUsername(){
        List<ReviewResponse> reviews = reviewService.getAllReviewsByUsername();
        return ResponseEntity.ok(reviews);
    }
}
