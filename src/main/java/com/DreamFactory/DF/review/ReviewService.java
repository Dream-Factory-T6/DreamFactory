package com.DreamFactory.DF.review;

import com.DreamFactory.DF.review.dtos.ReviewMapper;
import com.DreamFactory.DF.review.dtos.ReviewResponse;
import com.DreamFactory.DF.user.UserRepository;
import com.DreamFactory.DF.user.dto.UserMapper;
import com.DreamFactory.DF.user.dto.UserResponse;
import com.DreamFactory.DF.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private  final  ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public List<ReviewResponse> getAllReviewsByUsername() {
        String username = getAuthenticatedUser().username();

        List<Review> reviews = reviewRepository.findByUsername(username);

        return reviews.stream()
                .map(ReviewMapper::toReviewResponse)
                .toList();
    }

    public UserResponse getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        String username = authentication.getName();

        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return UserMapper.fromEntity(user);
    }
}
