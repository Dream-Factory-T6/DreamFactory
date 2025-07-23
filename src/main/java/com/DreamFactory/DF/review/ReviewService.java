package com.DreamFactory.DF.review;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.destination.DestinationService;
import com.DreamFactory.DF.review.dtos.ReviewMapper;
import com.DreamFactory.DF.review.dtos.ReviewRequest;
import com.DreamFactory.DF.review.dtos.ReviewResponse;
import com.DreamFactory.DF.review.exceptions.ReviewNotFoundByIdException;
import com.DreamFactory.DF.user.UserService;
import com.DreamFactory.DF.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final DestinationService destinationService;

    public List<ReviewResponse> getAllReviewsByUsername() {
        String username = userService.getAuthenticatedUser().getUsername();
        List<Review> reviews = reviewRepository.findByUserUsername(username);

        return reviews.stream()
                .map(ReviewMapper::toReviewResponse)
                .toList();
    }

    public List<ReviewResponse> getAllReviewsByDestinationId(Long id) {
        Destination destination = destinationService.getDestObjById(id);

        List<Review> reviews = reviewRepository.findByDestinationId(id);

        return reviews.stream()
                .map(ReviewMapper::toReviewResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse createReview(ReviewRequest request) {
        User user = userService.getAuthenticatedUser();
        Destination destination = destinationService.getDestObjById(request.destinationId());

        Review newReview = ReviewMapper.toEntity(request);
        newReview.setUser(user);
        newReview.setDestination(destination);
        reviewRepository.save(newReview);

        return ReviewMapper.toReviewResponse(newReview);
    }

    @Transactional
    public ReviewResponse updateReview(Long id, ReviewRequest request) {
        User user = userService.getAuthenticatedUser();
        Destination destination = destinationService.getDestObjById(request.destinationId());

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundByIdException(id));

        if (!review.getUser().equals(user)) {
            throw new AccessDeniedException("You are not allowed to update this review.");
        }

        review.setRating(request.rating());
        review.setBody(request.body().trim());
        review.setDestination(destination);

        return ReviewMapper.toReviewResponse(review);
    }

    public void deleteReview(Long id) {
        User user = userService.getAuthenticatedUser();
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundByIdException(id));

        if (!review.getUser().equals(user)) {
            throw new AccessDeniedException("You are not allowed to update this review.");
        }
        reviewRepository.deleteById(id);
    }
}
