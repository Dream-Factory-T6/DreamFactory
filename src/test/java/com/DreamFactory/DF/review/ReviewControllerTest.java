package com.DreamFactory.DF.review;

import com.DreamFactory.DF.review.dtos.ReviewRequest;
import com.DreamFactory.DF.review.dtos.ReviewResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReviewService reviewService;

    private ReviewResponse reviewResponse;

    @BeforeEach
    public void setUp() {
        reviewResponse = new ReviewResponse(1L,
                4.5,
                "Nice place!",
                LocalDateTime.now(),
                "testUser");
    }

    @Test
    void showReviewsByUsernameTest_Success() throws Exception {
        Mockito.when(reviewService.getAllReviewsByUsername()).thenReturn(List.of(reviewResponse));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].body").value("Nice place!"))
                .andExpect(jsonPath("$[0].username").value("testUser"));
    }

    @Test
    void showReviewsByDestinationIdTest_Success() throws Exception {
        Mockito.when(reviewService.getAllReviewsByDestinationId(100L)).thenReturn(List.of(reviewResponse));

        mockMvc.perform(get("/api/reviews/destination/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].body").value("Nice place!"));
    }

    @Test
    void storeReviewTest_Success() throws Exception {
        ReviewRequest request = new ReviewRequest(4.5, "Nice place!", 100L);

        Mockito.when(reviewService.createReview(Mockito.any())).thenReturn(reviewResponse);

        mockMvc.perform(post("/api/reviews").contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body").value("Nice place!"));
    }

    @Test
    void updateReviewTest_Success() throws Exception {
        ReviewRequest request = new ReviewRequest(4.8, "Updated text.", 100L);
        ReviewResponse updatedResponse = new ReviewResponse(1L, 4.8, "Updated text.", LocalDateTime.now(), "testUser");

        Mockito.when(reviewService.updateReview(Mockito.eq(1L), Mockito.any()))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value("Updated text."))
                .andExpect(jsonPath("$.rating").value(4.8));
    }

    @Test
    void deleteReviewTest_Success() throws Exception {
        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(reviewService).delete(1L);
    }
}