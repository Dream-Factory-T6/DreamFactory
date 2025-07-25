package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.destination.dto.DestinationFilterRequest;
import com.DreamFactory.DF.destination.dto.DestinationRequest;
import com.DreamFactory.DF.destination.dto.DestinationResponse;
import com.DreamFactory.DF.destination.dto.DestinationWithReviewsResponse;
import com.DreamFactory.DF.destination.exceptions.DestinationNotFoundException;
import com.DreamFactory.DF.user.UserRepository;
import com.DreamFactory.DF.user.model.User;
import com.DreamFactory.DF.role.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DestinationController.class)
@Import(TestSecurityConfig.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class DestinationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private DestinationService destinationService;

        @MockitoBean
        private UserRepository userRepository;

        @Autowired
        private ObjectMapper objectMapper;

        private User testUser;
        private DestinationWithReviewsResponse testDestinationWithReviewsResponse;
        private DestinationResponse testDestinationResponse;

        @BeforeEach
        void setUp() {
                testUser = User.builder()
                        .id(1L)
                        .username("testuser")
                        .email("test@example.com")
                        .password("password")
                        .roles(Set.of(Role.USER))
                        .build();

                testDestinationWithReviewsResponse = new DestinationWithReviewsResponse(
                        1L,
                        "Test Destination",
                        "Test Location",
                        "Test Description",
                        "https://example.com/image.jpg",
                        "testuser",
                        List.of(),
                        4.7,
                        LocalDateTime.now(),
                        LocalDateTime.now());

                testDestinationResponse = new DestinationResponse(
                        1L,
                        "Test Destination",
                        "Test Location",
                        "Test Description",
                        "https://example.com/image.jpg",
                        "testuser",
                        4.7,
                        LocalDateTime.now(),
                        LocalDateTime.now());
        }

        @Nested
        class GetAllDestinations {
                @Test
                void shouldReturnPaginatedDestinations() throws Exception {
                        Page<DestinationResponse> destinationPage = new PageImpl<>(
                                List.of(testDestinationResponse),
                                PageRequest.of(0, 4),
                                1);
                        when(destinationService.getAllDestinations(0, 4)).thenReturn(destinationPage);

                        mockMvc.perform(get("/api/destinations")
                                        .param("page", "1")
                                        .param("size", "4"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content[0].id").value(1))
                                .andExpect(jsonPath("$.content[0].title").value("Test Destination"))
                                .andExpect(jsonPath("$.content[0].location").value("Test Location"))
                                .andExpect(jsonPath("$.content[0].rating").value(4.7))
                                .andExpect(jsonPath("$.totalElements").value(1))
                                .andExpect(jsonPath("$.totalPages").value(1));

                        verify(destinationService).getAllDestinations(0, 4);
                }
        }

        @Nested
        class GetDestinationById {
                @Test
                void shouldReturnDestinationWhenFound() throws Exception {
                        when(destinationService.getDestinationById(1L)).thenReturn(testDestinationWithReviewsResponse);

                        mockMvc.perform(get("/api/destinations/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Test Destination"))
                                .andExpect(jsonPath("$.location").value("Test Location"))
                                .andExpect(jsonPath("$.rating").value(4.7));

                        verify(destinationService).getDestinationById(1L);
                }

                @Test
                void shouldReturn404WhenDestinationNotFound() throws Exception {
                        when(destinationService.getDestinationById(999L))
                                .thenThrow(new DestinationNotFoundException(999L));

                        mockMvc.perform(get("/api/destinations/999"))
                                .andExpect(status().isNotFound());

                        verify(destinationService).getDestinationById(999L);
                }

                @Test
                void shouldHandleInvalidIdFormat() throws Exception {
                        mockMvc.perform(get("/api/destinations/invalid"))
                                .andExpect(status().isBadRequest());
                }
        }

        @Nested
        class GetDestinationsWithFilters {
                @Test
                void shouldFilterByLocationOnly() throws Exception {
                        Page<DestinationResponse> destinationPage = new PageImpl<>(
                                List.of(testDestinationResponse),
                                PageRequest.of(0, 4),
                                1);
                        when(destinationService.getDestinationsWithFilters(any(DestinationFilterRequest.class), eq(0),
                                eq(4)))
                                .thenReturn(destinationPage);

                        mockMvc.perform(get("/api/destinations/filter")
                                        .param("location", "Paris")
                                        .param("page", "1")
                                        .param("size", "4"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray());

                        verify(destinationService).getDestinationsWithFilters(
                                argThat(filter -> "Paris".equals(filter.location()) && filter.title() == null),
                                eq(0), eq(4));
                }
        }

        @Nested
        class GetMyDestinations {
                @Test
                @WithMockUser(username = "testuser")
                void shouldReturnUserDestinationsWhenAuthenticated() throws Exception {
                        Page<DestinationResponse> destinationPage = new PageImpl<>(
                                List.of(testDestinationResponse),
                                PageRequest.of(0, 4),
                                1);
                        when(destinationService.getUserDestinations(eq(0), eq(4), eq(null)))
                                .thenReturn(destinationPage);

                        mockMvc.perform(get("/api/destinations/my-destinations")
                                        .param("page", "1")
                                        .param("size", "4"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content[0].id").value(1));

                        verify(destinationService).getUserDestinations(eq(0), eq(4), eq(null));
                }

                @Test
                void shouldReturn403WhenNotAuthenticated() throws Exception {
                        doThrow(new com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException(
                                "Not authenticated")).when(destinationService)
                                .getUserDestinations(eq(0), eq(4), eq(null));
                        mockMvc.perform(get("/api/destinations/my-destinations")
                                        .param("page", "1")
                                        .param("size", "4"))
                                .andExpect(status().isForbidden());
                }

                @Test
                @WithMockUser(username = "testuser")
                void shouldHandleUserNotFound() throws Exception {
                        doThrow(new com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException(
                                "User not found")).when(destinationService)
                                .getUserDestinations(eq(0), eq(4), eq(null));

                        mockMvc.perform(get("/api/destinations/my-destinations")
                                        .param("page", "1")
                                        .param("size", "4"))
                                .andExpect(status().isForbidden());
                }
        }

        @Nested
        class CreateDestination {
                @Test
                @WithMockUser(username = "testuser")
                void shouldCreateDestinationSuccessfully() throws Exception {
                        when(destinationService.createDestination(any(DestinationRequest.class)))
                                .thenReturn(testDestinationResponse);

                        MockMultipartFile image = new MockMultipartFile(
                                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

                        mockMvc.perform(multipart("/api/destinations")
                                        .file(image)
                                        .param("title", "Test Destination")
                                        .param("location", "Test Location")
                                        .param("description", "Test Description")
                                        .with(csrf()))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Test Destination"))
                                .andExpect(jsonPath("$.location").value("Test Location"));

                        verify(destinationService).createDestination(any(DestinationRequest.class));
                }

                @Test
                @WithMockUser(username = "testuser")
                void shouldHandleUserNotFoundDuringCreation() throws Exception {
                        doThrow(new com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException(
                                "User not found")).when(destinationService)
                                .createDestination(any(DestinationRequest.class));

                        MockMultipartFile image = new MockMultipartFile(
                                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

                        mockMvc.perform(multipart("/api/destinations")
                                        .file(image)
                                        .param("title", "Test Destination")
                                        .param("location", "Test Location")
                                        .param("description", "Test Description")
                                        .with(csrf()))
                                .andExpect(status().isForbidden());
                }

                @Test
                void shouldReturn403WhenNotAuthenticatedForCreation() throws Exception {
                        doThrow(new com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException(
                                "Not authenticated")).when(destinationService)
                                .createDestination(any(DestinationRequest.class));
                        MockMultipartFile image = new MockMultipartFile(
                                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

                        mockMvc.perform(multipart("/api/destinations")
                                        .file(image)
                                        .param("title", "Test Destination")
                                        .param("location", "Test Location")
                                        .param("description", "Test Description")
                                        .with(csrf()))
                                .andExpect(status().isForbidden());
                }

                @Test
                @WithMockUser(username = "testuser")
                void shouldHandleMissingRequiredFields() throws Exception {
                        MockMultipartFile image = new MockMultipartFile(
                                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

                        mockMvc.perform(multipart("/api/destinations")
                                        .file(image)
                                        .param("location", "Test Location")
                                        .param("description", "Test Description")
                                        .with(csrf()))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(
                                        org.hamcrest.Matchers.containsString("Title is required")));
                }
        }

        @Nested
        class UpdateDestination {
                @Test
                @WithMockUser(username = "testuser")
                void shouldUpdateDestinationSuccessfully() throws Exception {
                        when(destinationService.updateDestination(eq(1L), any(DestinationRequest.class)))
                                .thenReturn(testDestinationResponse);

                        MockMultipartFile image = new MockMultipartFile(
                                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

                        mockMvc.perform(multipart("/api/destinations/1")
                                        .file(image)
                                        .param("title", "Updated Destination")
                                        .param("location", "Updated Location")
                                        .param("description", "Updated Description")
                                        .with(request -> {
                                                request.setMethod("PUT");
                                                return request;
                                        })
                                        .with(csrf()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("Test Destination"));

                        verify(destinationService).updateDestination(eq(1L), any(DestinationRequest.class));
                }

                @Test
                @WithMockUser(username = "testuser")
                void shouldHandleDestinationNotFoundDuringUpdate() throws Exception {
                        when(destinationService.updateDestination(eq(999L), any(DestinationRequest.class)))
                                .thenThrow(new DestinationNotFoundException(999L));

                        MockMultipartFile image = new MockMultipartFile(
                                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

                        mockMvc.perform(multipart("/api/destinations/999")
                                        .file(image)
                                        .param("title", "Updated Destination")
                                        .param("location", "Updated Location")
                                        .param("description", "Updated Description")
                                        .with(request -> {
                                                request.setMethod("PUT");
                                                return request;
                                        })
                                        .with(csrf()))
                                .andExpect(status().isNotFound());

                        verify(destinationService).updateDestination(eq(999L), any(DestinationRequest.class));
                }

                @Test
                @WithMockUser(username = "testuser")
                void shouldHandleUserNotFoundDuringUpdate() throws Exception {
                        doThrow(new com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException(
                                "User not found")).when(destinationService)
                                .updateDestination(eq(1L), any(DestinationRequest.class));

                        MockMultipartFile image = new MockMultipartFile(
                                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

                        mockMvc.perform(multipart("/api/destinations/1")
                                        .file(image)
                                        .param("title", "Updated Destination")
                                        .param("location", "Updated Location")
                                        .param("description", "Updated Description")
                                        .with(request -> {
                                                request.setMethod("PUT");
                                                return request;
                                        })
                                        .with(csrf()))
                                .andExpect(status().isForbidden());
                }

                @Test
                void shouldReturn403WhenNotAuthenticatedForUpdate() throws Exception {
                        doThrow(new com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException(
                                "Not authenticated")).when(destinationService)
                                .updateDestination(eq(1L), any(DestinationRequest.class));
                        MockMultipartFile image = new MockMultipartFile(
                                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

                        mockMvc.perform(multipart("/api/destinations/1")
                                        .file(image)
                                        .param("title", "Updated Destination")
                                        .param("location", "Updated Location")
                                        .param("description", "Updated Description")
                                        .with(request -> {
                                                request.setMethod("PUT");
                                                return request;
                                        })
                                        .with(csrf()))
                                .andExpect(status().isForbidden());
                }
        }

        @Nested
        class DeleteDestination {
                @Test
                @WithMockUser(username = "testuser")
                void shouldDeleteDestinationSuccessfully() throws Exception {
                        doNothing().when(destinationService).deleteDestination(1L);

                        mockMvc.perform(delete("/api/destinations/1")
                                        .with(csrf()))
                                .andExpect(status().isNoContent());

                        verify(destinationService).deleteDestination(1L);
                }

                @Test
                @WithMockUser(username = "testuser")
                void shouldReturn404WhenDestinationNotFoundForDeletion() throws Exception {
                        doThrow(new DestinationNotFoundException(999L))
                                .when(destinationService).deleteDestination(999L);

                        mockMvc.perform(delete("/api/destinations/999")
                                        .with(csrf()))
                                .andExpect(status().isNotFound());

                        verify(destinationService).deleteDestination(999L);
                }

                @Test
                @WithMockUser(username = "testuser")
                void shouldHandleUserNotFoundDuringDeletion() throws Exception {
                        doThrow(new com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException(
                                "User not found")).when(destinationService).deleteDestination(1L);

                        mockMvc.perform(delete("/api/destinations/1")
                                        .with(csrf()))
                                .andExpect(status().isForbidden());
                }

                @Test
                void shouldReturn403WhenNotAuthenticatedForDeletion() throws Exception {
                        doThrow(new com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException(
                                "Not authenticated")).when(destinationService).deleteDestination(1L);
                        mockMvc.perform(delete("/api/destinations/1")
                                        .with(csrf()))
                                .andExpect(status().isForbidden());
                }

                @Test
                @WithMockUser(username = "testuser")
                void shouldHandleInvalidIdFormatForDeletion() throws Exception {
                        mockMvc.perform(delete("/api/destinations/invalid")
                                        .with(csrf()))
                                .andExpect(status().isBadRequest());
                }
        }
}

