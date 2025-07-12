package com.DreamFactory.DF.review;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.destination.DestinationRepository;
import com.DreamFactory.DF.review.dtos.ReviewMapper;
import com.DreamFactory.DF.review.dtos.ReviewRequest;
import com.DreamFactory.DF.review.dtos.ReviewResponse;
import com.DreamFactory.DF.user.UserRepository;
import com.DreamFactory.DF.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private  final  ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;

    public List<ReviewResponse> getAllReviewsByUsername() {
        String username = getAuthenticatedUser().getUsername();

        List<Review> reviews = reviewRepository.findByUserUsername(username);

        return reviews.stream()
                .map(ReviewMapper::toReviewResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse createReview(ReviewRequest request){
        User user = getAuthenticatedUser();

        Destination destination = destinationRepository.findById(request.destinationId())
                .orElseThrow(() -> new RuntimeException("Destination not found by id: " + request.destinationId()));

        Review newReview = ReviewMapper.toEntity(request);
        newReview.setUser(user);
        newReview.setDestination(destination);

        reviewRepository.save(newReview);

        return ReviewMapper.toReviewResponse(newReview);
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
