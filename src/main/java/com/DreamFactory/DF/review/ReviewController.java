package com.DreamFactory.DF.review;


import com.DreamFactory.DF.review.dtos.ReviewRequest;
import com.DreamFactory.DF.review.dtos.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final ReviewService reviewService;

    @GetMapping("")
    @Operation(summary = "Get all reviews by user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reviews returned successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/UserNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<List<ReviewResponse>> showReviewsByUsername() {
        List<ReviewResponse> reviews = reviewService.getAllReviewsByUsername();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("")
    @Operation(summary = "Post new review for destination.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Review created successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/UserNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<ReviewResponse> storeReview(@Valid @RequestBody ReviewRequest request) {
        ReviewResponse newReview = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update review for destination.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review updated successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        ReviewResponse updatedReview = reviewService.updateReview(id, request);
        return ResponseEntity.ok(updatedReview);
    }
    @DeleteMapping
    @Operation(summary = "Delete review for destination.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<ReviewResponse> deleteReview(@PathVariable Long id){
        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
