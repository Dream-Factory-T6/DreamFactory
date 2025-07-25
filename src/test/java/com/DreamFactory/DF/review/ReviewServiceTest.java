package com.DreamFactory.DF.review;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.destination.DestinationService;
import com.DreamFactory.DF.review.dtos.ReviewRequest;
import com.DreamFactory.DF.review.dtos.ReviewResponse;
import com.DreamFactory.DF.review.exceptions.ReviewNotFoundByIdException;
import com.DreamFactory.DF.user.UserService;
import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

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
    private UserService userService;
    @Mock
    private DestinationService destinationService;

    @InjectMocks
    private ReviewService reviewService;

    private User testUser;
    private Destination testDestination;
    private Review testReview;
    private ReviewRequest reviewRequest;

    @BeforeEach
    public void setUp() {
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
    void getAllReviewsByUsernameTest_Success() {
        Mockito.doReturn(testUser).when(userService).getAuthenticatedUser();
        Mockito.when(reviewRepository.findByUserUsername("testUser"))
                .thenReturn(List.of(testReview));

        List<ReviewResponse> responses = reviewService.getAllReviewsByUsername();

        assertEquals(1, responses.size());
        ReviewResponse response = responses.get(0);
        assertEquals(testUser.getUsername(), response.username());
        assertEquals(testReview.getBody(), response.body());
        assertEquals(testReview.getRating(), response.rating());
    }

    @Test
    void getAllReviewsByUsernameTest_Empty() {
        Mockito.doReturn(testUser).when(userService).getAuthenticatedUser();
        Mockito.when(reviewRepository.findByUserUsername("testUser"))
                .thenReturn(Collections.emptyList());

        List<ReviewResponse> responses = reviewService.getAllReviewsByUsername();

        assertTrue(responses.isEmpty());
    }

    @Test
    void getReviewsByDestinationIdTest_Success() {
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(reviewRepository.findByDestinationId(100L)).thenReturn(List.of(testReview));

        List<ReviewResponse> responses = reviewService.getAllReviewsByDestinationId(100L);

        assertEquals(1, responses.size());
        assertEquals("Good Location!", responses.get(0).body());
    }

    @Test
    void getReviewsByDestinationIdTest_Empty() {
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(reviewRepository.findByDestinationId(100L)).thenReturn(Collections.emptyList());

        List<ReviewResponse> responses = reviewService.getAllReviewsByDestinationId(100L);

        assertTrue(responses.isEmpty());
    }

    @Test
    void createReviewTest() {
        Mockito.doReturn(testUser).when(userService).getAuthenticatedUser();
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(reviewRepository.save(any(Review.class))).thenReturn(testReview);

        ReviewResponse response = reviewService.createReview(reviewRequest);

        assertNotNull(response);
        assertEquals(4.5, response.rating());
        assertEquals("Good Location!", response.body());
    }

    @Test
    void updateReviewTest_Success() {
        ReviewRequest updateRequest = new ReviewRequest(5.0, "Updated review", 100L);

        Mockito.doReturn(testUser).when(userService).getAuthenticatedUser();
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview));

        ReviewResponse response = reviewService.updateReview(1L, updateRequest);

        assertEquals(5.0, response.rating());
        assertEquals("Updated review", response.body());
        assertEquals(testUser.getUsername(), response.username());
    }

    @Test
    void updateReviewTest_AccessDenied() {
        User anotherUser = new User();
        anotherUser.setId(99L);
        anotherUser.setUsername("other");
        Review someoneElsesReview = Review.builder()
                .id(1L)
                .user(anotherUser)
                .destination(testDestination)
                .rating(3.0)
                .body("Someone else's review")
                .build();

        ReviewRequest request = new ReviewRequest(5.0, "Hack attempt", 100L);

        Mockito.doReturn(testUser).when(userService).getAuthenticatedUser();
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(reviewRepository.findById(1L)).thenReturn(Optional.of(someoneElsesReview));

        assertThrows(AccessDeniedException.class,
                () -> reviewService.updateReview(1L, request));
    }

    @Test
    void deleteReviewTest() {
        Mockito.doReturn(testUser).when(userService).getAuthenticatedUser();
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundByIdException.class,
                () -> reviewService.updateReview(1L, reviewRequest));
    }

    @Test
    void deleteReviewTest_ReviewNotFound() {
        Long reviewId = 99L;

        Mockito.doReturn(testUser).when(userService).getAuthenticatedUser();
        Mockito.when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundByIdException.class, () -> reviewService.deleteReview(reviewId));
    }
}