package com.DreamFactory.DF.review;

import com.DreamFactory.DF.review.dtos.ReviewResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
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
    void showReviewsByUsername_Success() throws Exception {
        Mockito.when(reviewService.getAllReviewsByUsername()).thenReturn(List.of(reviewResponse));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].body").value("Nice place!"))
                .andExpect(jsonPath("$[0].username").value("testUser"));
    }
}