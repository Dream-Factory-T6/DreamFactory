package com.DreamFactory.DF.like;

import com.DreamFactory.DF.like.dtos.DestinationLikeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(DestinationLikeController.class)
@AutoConfigureMockMvc(addFilters = false)
class DestinationLikeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DestinationLikeService likeService;

    private Long destinationId;
    private DestinationLikeResponse response;

    @BeforeEach
    public void setUp() {
        destinationId = 1L;
        response = new DestinationLikeResponse(100L, true, 1L);
    }

    @Test
    void toggleLikeTest_ShouldReturnOkWithResponse() throws Exception {
        Mockito.when(likeService.toggleLike(destinationId)).thenReturn(response);

        mockMvc.perform(post("/api/destinations/{id}/likes/toggle", destinationId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getLikeByDestinationIdTest_ShouldReturnOkWithResponse() throws Exception {
        Mockito.when(likeService.getLikeByDestinationId(destinationId)).thenReturn(response);

        mockMvc.perform(get("/api/destinations/{id}/likes", destinationId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}