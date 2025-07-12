package com.DreamFactory.DF.review;


import com.DreamFactory.DF.review.dtos.ReviewRequest;
import com.DreamFactory.DF.review.dtos.ReviewResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("")
    public ResponseEntity<ReviewResponse> storeReview(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse newReview = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request){
        ReviewResponse updatedReview = reviewService.updateReview(id,request);
        return ResponseEntity.ok(updatedReview);
    }
}
