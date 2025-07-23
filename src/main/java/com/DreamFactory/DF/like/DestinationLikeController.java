package com.DreamFactory.DF.like;

import com.DreamFactory.DF.like.dtos.DestinationLikeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/destinations/{id}/likes")
public class DestinationLikeController {
    private final DestinationLikeService likeService;

    @PostMapping("/toggle")
    @Operation(summary = "Adding/Deleting destination Like.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Like/Unlike successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    public ResponseEntity<DestinationLikeResponse> toggleLike(@PathVariable("id") Long destinationId) {
        return ResponseEntity.ok(likeService.toggleLike(destinationId));
    }

    @Operation(summary = "Getting all destination Likes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get all likes successfully"),
                    @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
                    @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
                    @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
                    @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
                    @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
            })
    @GetMapping
    public ResponseEntity<DestinationLikeResponse> getLikeByDestinationId(@PathVariable("id") Long destinationId) {
        return ResponseEntity.ok(likeService.getLikeByDestinationId(destinationId));
    }
}

