package com.DreamFactory.DF.review;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.destination.DestinationRepository;
import com.DreamFactory.DF.review.dtos.ReviewRequest;
import com.DreamFactory.DF.review.dtos.ReviewResponse;
import com.DreamFactory.DF.user.UserRepository;
import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DestinationRepository destinationRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User testUser;
    private Destination testDestination;
    private Review testReview;
    private ReviewRequest reviewRequest;

    @BeforeEach
    public void setUp() {
        reviewService = Mockito.spy(new ReviewService(reviewRepository,
                userRepository,
                destinationRepository));

        testUser = new User();
        testUser.setId(10L);
        testUser.setUsername("testUser");

        testDestination = new Destination();
        testDestination.setId(100L);
        testDestination.setTitle("Test Destination");
        testDestination.setLocation("Test Location");
        testDestination.setDescription("Test Description");

        testReview = Review.builder()
                .id(1L)
                .rating(4.5)
                .body("Good Location!")
                .destination(testDestination)
                .user(testUser)
                .build();

        reviewRequest = new ReviewRequest(4.5, "Good Location!", 100L);
    }

    @Test
    void getReviewsByDestinationIdTest_Success() {
        Mockito.when(destinationRepository.findById(100L)).thenReturn(Optional.of(testDestination));
        Mockito.when(reviewRepository.findByDestinationId(100L)).thenReturn(List.of(testReview));

        List<ReviewResponse> responses = reviewService.getAllReviewsByDestinationId(100L);

        assertEquals(1, responses.size());
        assertEquals("Good Location!", responses.get(0).body());
    }

    @Test
    void getReviewsByDestinationIdTest_Empty() {
        Mockito.when(destinationRepository.findById(100L)).thenReturn(Optional.of(testDestination));
        Mockito.when(reviewRepository.findByDestinationId(100L)).thenReturn(Collections.emptyList());

        List<ReviewResponse> responses = reviewService.getAllReviewsByDestinationId(100L);

        assertTrue(responses.isEmpty());
    }

    @Test
    void createReviewTest() {
        Mockito.doReturn(testUser).when(reviewService).getAuthenticatedUser();
        Mockito.when(destinationRepository.findById(100L)).thenReturn(Optional.of(testDestination));
        Mockito.when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        ReviewResponse response = reviewService.createReview(reviewRequest);

        assertNotNull(response);
        assertEquals(4.5, response.rating());
        assertEquals("Good Location!", response.body());
    }
}