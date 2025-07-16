package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.destination.dto.DestinationFilterRequest;
import com.DreamFactory.DF.destination.dto.DestinationRequest;
import com.DreamFactory.DF.destination.dto.DestinationResponse;
import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
@Tag(name = "Destination", description = "Operations related to destination")
public class DestinationController {
    private final DestinationService destinationService;
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Get all destinations.", responses = {
            @ApiResponse(responseCode = "200", description = "Destinations returned successfully"),
            @ApiResponse(responseCode = "204", description = "No destinations found"),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<Page<DestinationResponse>> getAllDestinations(
            @Parameter(description = "Page number (starts from 1)", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size", example = "4") @RequestParam(defaultValue = "4") int size) {
        Page<DestinationResponse> destinations = destinationService.getAllDestinations(convertToZeroBasedPage(page),
                size);

        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get destination by ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Destination returned successfully"),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<DestinationResponse> getDestinationById(@PathVariable Long id) {
        DestinationResponse destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(destination);
    }

    @GetMapping("/filter")
    @Operation(summary = "Get destination with filter by title or by location.", responses = {
            @ApiResponse(responseCode = "200", description = "Destination returned successfully"),
            @ApiResponse(responseCode = "204", description = "No destinations found"),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<Page<DestinationResponse>> getDestinationsWithFilters(
            @Parameter(description = "Filter by location", required = false) @RequestParam(required = false) String location,
            @Parameter(description = "Filter by title", required = false) @RequestParam(required = false) String title,
            @Parameter(description = "Page number (starts from 1)", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size", example = "4") @RequestParam(defaultValue = "4") int size) {
        DestinationFilterRequest filter = new DestinationFilterRequest(location, title);
        Page<DestinationResponse> destinations = destinationService.getDestinationsWithFilters(filter,

                convertToZeroBasedPage(page), size);
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/my-destinations")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get destinations of User.", responses = {
            @ApiResponse(responseCode = "200", description = "Destinations returned successfully"),
            @ApiResponse(responseCode = "204", description = "No destinations found"),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/UserNotFound"),
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<Page<DestinationResponse>> getMyDestinations(
            @Parameter(description = "Page number (starts from 1)", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Page size", example = "4") @RequestParam(defaultValue = "4") int size,
            @Parameter(description = "Sort by creation date: 'asc' or 'desc'", required = false, example = "asc") @RequestParam(required = false) String sort) {
        User currentUser = getCurrentUser();

        Page<DestinationResponse> destinations = destinationService.getUserDestinations(currentUser,
                convertToZeroBasedPage(page), size, sort);
        return ResponseEntity.ok(destinations);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Post new destination.", responses = {
            @ApiResponse(responseCode = "201", description = "Destination created successfully"),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/UserNotFound"),
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<DestinationResponse> createDestination(
            @Valid @RequestBody DestinationRequest request) {
        User currentUser = getCurrentUser();
        DestinationResponse createdDestination = destinationService.createDestination(currentUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDestination);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update destination.", responses = {
            @ApiResponse(responseCode = "200", description = "Destination updated successfully"),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<DestinationResponse> updateDestination(
            @PathVariable Long id,
            @Valid @RequestBody DestinationRequest request) {
        User currentUser = getCurrentUser();
        DestinationResponse updatedDestination = destinationService.updateDestination(id, currentUser, request);
        return ResponseEntity.ok(updatedDestination);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete destination.", responses = {
            @ApiResponse(responseCode = "204", description = "Destination deleted successfully"),
            @ApiResponse(responseCode = "400", ref = "#/components/responses/BadRequest"),
            @ApiResponse(responseCode = "401", ref = "#/components/responses/Unauthorized"),
            @ApiResponse(responseCode = "403", ref = "#/components/responses/Forbidden"),
            @ApiResponse(responseCode = "404", ref = "#/components/responses/DestinationNotFound"),
            @ApiResponse(responseCode = "500", ref = "#/components/responses/InternalServerError")
    })
    public ResponseEntity<Void> deleteDestination(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        destinationService.deleteDestination(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("User not found"));
    }

    private int convertToZeroBasedPage(int page) {
        return page - 1;
    }
}
