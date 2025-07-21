package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.cloudinary.CloudinaryService;
import com.DreamFactory.DF.destination.dto.*;
import com.DreamFactory.DF.destination.exceptions.DestinationNotFoundException;
import com.DreamFactory.DF.destination.exceptions.UnauthorizedAccessException;
import com.DreamFactory.DF.email.EmailService;
import com.DreamFactory.DF.exceptions.EmailSendException;
import com.DreamFactory.DF.user.model.User;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DestinationServiceTest {

    @Mock
    private DestinationRepository destinationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private DestinationService destinationService;

    private User testUser;
    private Destination testDestination;
    private DestinationRequest testDestinationRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        testDestination = Destination.builder()
                .id(1L)
                .title("Test Destination")
                .location("Test Location")
                .description("Test Description")
                .imageUrl("http://test.com/image.jpg")
                .user(testUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testDestinationRequest = new DestinationRequest(
                "New Destination",
                "New Location",
                "New Description",
                new MultipartFileMock());
    }

    @Nested
    class GetAllDestinationsTests {

        @Test
        void shouldReturnPaginatedDestinations() {
            Page<Destination> destinationPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findAll(any(Pageable.class))).thenReturn(destinationPage);

            Page<DestinationResponse> result = destinationService.getAllDestinations(0, 10);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().getFirst().title()).isEqualTo("Test Destination");
            assertThat(result.getContent().getFirst().location()).isEqualTo("Test Location");

            verify(destinationRepository).findAll(any(Pageable.class));
        }

        @Test
        void shouldReturnEmptyPageWhenNoDestinations() {
            Page<Destination> emptyPage = new PageImpl<>(Collections.emptyList());
            when(destinationRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

            Page<DestinationResponse> result = destinationService.getAllDestinations(0, 10);

            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isZero();
        }
    }

    @Nested
    class GetDestinationByIdTests {
        @Test
        void shouldReturnDestinationWhenFound() {
            when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));

            DestinationResponse result = destinationService.getDestinationById(1L);

            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo("Test Destination");
            assertThat(result.location()).isEqualTo("Test Location");

            verify(destinationRepository).findById(1L);
        }

        @Test
        void shouldThrowExceptionWhenDestinationNotFound() {
            when(destinationRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> destinationService.getDestinationById(999L))
                    .isInstanceOf(DestinationNotFoundException.class)
                    .hasMessageContaining("999");
        }
    }

    @Nested
    class GetDestinationsWithFiltersTests {

        @Test
        void shouldFilterByLocationAndTitle() {
            DestinationFilterRequest filter = new DestinationFilterRequest("Paris", "Eiffel");
            Page<Destination> filteredPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findByLocationAndTitleContainingIgnoreCase(
                    eq("Paris"), eq("Eiffel"), any(Pageable.class)))
                    .thenReturn(filteredPage);

            Page<DestinationResponse> result = destinationService.getDestinationsWithFilters(filter, 0, 10);

            assertThat(result.getContent()).hasSize(1);
            verify(destinationRepository).findByLocationAndTitleContainingIgnoreCase(
                    eq("Paris"), eq("Eiffel"), any(Pageable.class));
        }

        @Test
        void shouldFilterByLocationOnly() {
            DestinationFilterRequest filter = new DestinationFilterRequest("Paris", null);
            Page<Destination> filteredPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findByLocationContainingIgnoreCase(
                    eq("Paris"), any(Pageable.class)))
                    .thenReturn(filteredPage);

            Page<DestinationResponse> result = destinationService.getDestinationsWithFilters(filter, 0, 10);

            assertThat(result.getContent()).hasSize(1);
            verify(destinationRepository).findByLocationContainingIgnoreCase(
                    eq("Paris"), any(Pageable.class));
        }

        @Test
        void shouldFilterByTitleOnly() {
            DestinationFilterRequest filter = new DestinationFilterRequest(null, "Eiffel");
            Page<Destination> filteredPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findByTitleContainingIgnoreCase(
                    eq("Eiffel"), any(Pageable.class)))
                    .thenReturn(filteredPage);

            Page<DestinationResponse> result = destinationService.getDestinationsWithFilters(filter, 0, 10);

            assertThat(result.getContent()).hasSize(1);
            verify(destinationRepository).findByTitleContainingIgnoreCase(
                    eq("Eiffel"), any(Pageable.class));
        }

        @Test
        void shouldReturnAllDestinationsWhenNoFilters() {
            DestinationFilterRequest filter = new DestinationFilterRequest(null, null);
            Page<Destination> allDestinationsPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findAll(any(Pageable.class))).thenReturn(allDestinationsPage);

            Page<DestinationResponse> result = destinationService.getDestinationsWithFilters(filter, 0, 10);

            assertThat(result.getContent()).hasSize(1);
            verify(destinationRepository).findAll(any(Pageable.class));
        }

        @Test
        void shouldTrimWhitespaceFromFilterValues() {
            DestinationFilterRequest filter = new DestinationFilterRequest("  Paris  ", "  Eiffel  ");
            Page<Destination> filteredPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findByLocationAndTitleContainingIgnoreCase(
                    eq("Paris"), eq("Eiffel"), any(Pageable.class)))
                    .thenReturn(filteredPage);

            Page<DestinationResponse> result = destinationService.getDestinationsWithFilters(filter, 0, 10);

            verify(destinationRepository).findByLocationAndTitleContainingIgnoreCase(
                    eq("Paris"), eq("Eiffel"), any(Pageable.class));
        }
    }

    @Nested
    class GetUserDestinationsTests {

        @Test
        void shouldReturnUserDestinationsWithoutSorting() {
            Page<Destination> userDestinationsPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findByUser(eq(testUser), any(Pageable.class)))
                    .thenReturn(userDestinationsPage);

            Page<DestinationResponse> result = destinationService.getUserDestinations(testUser, 0, 10);

            assertThat(result.getContent()).hasSize(1);
            verify(destinationRepository).findByUser(eq(testUser), any(Pageable.class));
        }

        @Test
        void shouldReturnUserDestinationsWithAscendingSort() {
            Page<Destination> userDestinationsPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findByUser(eq(testUser), any(Pageable.class)))
                    .thenReturn(userDestinationsPage);

            Page<DestinationResponse> result = destinationService.getUserDestinations(testUser, 0, 10, "asc");

            assertThat(result.getContent()).hasSize(1);
            verify(destinationRepository).findByUser(eq(testUser), any(Pageable.class));
        }

        @Test
        void shouldReturnUserDestinationsWithDescendingSort() {
            Page<Destination> userDestinationsPage = new PageImpl<>(List.of(testDestination));
            when(destinationRepository.findByUser(eq(testUser), any(Pageable.class)))
                    .thenReturn(userDestinationsPage);

            Page<DestinationResponse> result = destinationService.getUserDestinations(testUser, 0, 10, "desc");

            assertThat(result.getContent()).hasSize(1);
            verify(destinationRepository).findByUser(eq(testUser), any(Pageable.class));
        }
    }

    @Nested
    class CreateDestinationTests {

        @Test
        void shouldCreateDestinationSuccessfully() throws IOException, MessagingException {
            Map<String, String> cloudinaryResult = Map.of("secure_url", "http://cloudinary.com/image.jpg");
            when(cloudinaryService.uploadFile(any())).thenReturn(cloudinaryResult);
            when(destinationRepository.save(any(Destination.class))).thenAnswer(invocation -> {
                Destination saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });
            doNothing().when(emailService).sendDestinationCreatedEmail(
                    anyString(), anyString(), anyString(), anyString());

            DestinationResponse result = destinationService.createDestination(testUser, testDestinationRequest);

            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo("New Destination");
            assertThat(result.location()).isEqualTo("New Location");

            verify(cloudinaryService).uploadFile(any());
            verify(destinationRepository).save(any(Destination.class));
            verify(emailService).sendDestinationCreatedEmail(
                    eq(testUser.getEmail()), anyString(), anyString(), anyString());
        }

        @Test
        void shouldThrowEmailSendExceptionWhenEmailSendingFails() throws IOException, MessagingException {
            Map<String, String> cloudinaryResult = Map.of("secure_url", "http://cloudinary.com/image.jpg");
            when(cloudinaryService.uploadFile(any())).thenReturn(cloudinaryResult);
            when(destinationRepository.save(any(Destination.class))).thenAnswer(invocation -> {
                Destination saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });
            doThrow(new MessagingException("Email error")).when(emailService)
                    .sendDestinationCreatedEmail(anyString(), anyString(), anyString(), anyString());

            assertThatThrownBy(() -> destinationService.createDestination(testUser, testDestinationRequest))
                    .isInstanceOf(EmailSendException.class)
                    .hasMessageContaining("Failed to send confirmation email");
        }
    }

    @Nested
    class UpdateDestinationTests {

        @Test
        void shouldUpdateDestinationWhenUserIsAuthorized() throws IOException {
            when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));
            Map<String, String> cloudinaryResult = Map.of("secure_url", "http://cloudinary.com/new-image.jpg");
            when(cloudinaryService.uploadFile(any())).thenReturn(cloudinaryResult);

            DestinationResponse result = destinationService.updateDestination(1L, testUser, testDestinationRequest);

            assertThat(result).isNotNull();
            assertThat(result.title()).isEqualTo("New Destination");
            assertThat(result.location()).isEqualTo("New Location");

            verify(destinationRepository).findById(1L);
            verify(cloudinaryService).uploadFile(any());
        }

        @Test
        void shouldThrowExceptionWhenDestinationNotFound() {
            when(destinationRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> destinationService.updateDestination(999L, testUser, testDestinationRequest))
                    .isInstanceOf(DestinationNotFoundException.class);
        }

        @Test
        void shouldThrowUnauthorizedExceptionWhenUserNotAuthorized() {
            User unauthorizedUser = User.builder().id(999L).build();
            when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));

            assertThatThrownBy(() -> destinationService.updateDestination(1L, unauthorizedUser, testDestinationRequest))
                    .isInstanceOf(UnauthorizedAccessException.class);
        }
    }

    @Nested
    class DeleteDestinationTests {

        @Test
        void shouldDeleteDestinationWhenUserIsAuthorized() throws IOException {
            testDestination.setImageUrl("http://res.cloudinary.com/demo/image/upload/v1234/myimage.jpg");
            when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));
            doNothing().when(cloudinaryService).deleteFile(anyString());

            destinationService.deleteDestination(1L, testUser);

            verify(destinationRepository).findById(1L);
            verify(cloudinaryService).deleteFile("myimage");
            verify(destinationRepository).delete(testDestination);
        }

        @Test
        void shouldThrowExceptionWhenDestinationNotFound() {
            when(destinationRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> destinationService.deleteDestination(999L, testUser))
                    .isInstanceOf(DestinationNotFoundException.class);
        }

        @Test
        void shouldThrowUnauthorizedExceptionWhenUserNotAuthorized() {
            User unauthorizedUser = User.builder().id(999L).build();
            when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));

            assertThatThrownBy(() -> destinationService.deleteDestination(1L, unauthorizedUser))
                    .isInstanceOf(UnauthorizedAccessException.class);
        }

        @Test
        void shouldHandleImageUrlWithoutVersionPrefix() throws IOException {
            testDestination.setImageUrl("http://res.cloudinary.com/demo/image/upload/myimage.jpg");
            when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));
            doNothing().when(cloudinaryService).deleteFile(anyString());

            destinationService.deleteDestination(1L, testUser);

            verify(cloudinaryService).deleteFile("myimage");
        }

        @Test
        void shouldHandleImageUrlWithFileExtension() throws IOException {
            testDestination.setImageUrl("http://res.cloudinary.com/demo/image/upload/v1234/myimage.png");
            when(destinationRepository.findById(1L)).thenReturn(Optional.of(testDestination));
            doNothing().when(cloudinaryService).deleteFile(anyString());

            destinationService.deleteDestination(1L, testUser);

            verify(cloudinaryService).deleteFile("myimage");
        }
    }

    static class MultipartFileMock implements org.springframework.web.multipart.MultipartFile {
        @Override
        public String getName() {
            return "file";
        }

        @Override
        public String getOriginalFilename() {
            return "test-image.jpg";
        }

        @Override
        public String getContentType() {
            return "image/jpeg";
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return 1024;
        }

        @Override
        public byte[] getBytes() {
            return "test-image-data".getBytes();
        }

        @Override
        public java.io.InputStream getInputStream() {
            return new java.io.ByteArrayInputStream(getBytes());
        }

        @Override
        public void transferTo(java.io.File dest) {
        }
    }
}
