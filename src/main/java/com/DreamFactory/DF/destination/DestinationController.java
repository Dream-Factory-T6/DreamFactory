package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.destination.dto.DestinationFilterRequest;
import com.DreamFactory.DF.destination.dto.DestinationRequest;
import com.DreamFactory.DF.destination.dto.DestinationResponse;
import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.user.UserRepository;
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

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {
    private final DestinationService destinationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Page<DestinationResponse>> getAllDestinations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size) {
        Page<DestinationResponse> destinations = destinationService.getAllDestinations(convertToZeroBasedPage(page),
                size);
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationResponse> getDestinationById(@PathVariable Long id) {
        DestinationResponse destination = destinationService.getDestinationById(id);
        return ResponseEntity.ok(destination);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<DestinationResponse>> getDestinationsWithFilters(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size) {
        DestinationFilterRequest filter = new DestinationFilterRequest(location, title);
        Page<DestinationResponse> destinations = destinationService.getDestinationsWithFilters(filter,
                convertToZeroBasedPage(page), size);
        return ResponseEntity.ok(destinations);
    }

    @GetMapping("/my-destinations")
    public ResponseEntity<Page<DestinationResponse>> getMyDestinations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size) {
        User currentUser = getCurrentUser();
        Page<DestinationResponse> destinations = destinationService.getUserDestinations(currentUser,
                convertToZeroBasedPage(page), size);
        return ResponseEntity.ok(destinations);
    }

    @PostMapping
    public ResponseEntity<DestinationResponse> createDestination(
            @Valid @RequestBody DestinationRequest request) {
        User currentUser = getCurrentUser();
        DestinationResponse createdDestination = destinationService.createDestination(currentUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDestination);
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
