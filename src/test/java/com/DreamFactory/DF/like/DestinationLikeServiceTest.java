package com.DreamFactory.DF.like;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.destination.DestinationService;
import com.DreamFactory.DF.like.dtos.DestinationLikeMapper;
import com.DreamFactory.DF.like.dtos.DestinationLikeResponse;
import com.DreamFactory.DF.user.UserService;
import com.DreamFactory.DF.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DestinationLikeServiceTest {
    @Mock
    private DestinationLikeRepository likeRepository;

    @Mock
    private DestinationService destinationService;

    @Mock
    private UserService userService;

    @Mock
    private DestinationLikeMapper likeMapper;

    @InjectMocks
    private DestinationLikeService likeService;

    private User testUser;
    private Destination testDestination;
    private DestinationLikeResponse likeResponse;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setId(1L);

        testDestination = new Destination();
        testDestination.setId(100L);
        testDestination.setTitle("Test Destination");
        testDestination.setLocation("Test Location");
        testDestination.setDescription("Test Description");

        likeResponse = new DestinationLikeResponse(100L, true, 1L);
    }

    @Test
    public void toggleLikeTest() {
        Mockito.doReturn(testUser).when(userService).getAuthenticatedUser();
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(likeService.toggleLike(100L)).thenReturn(likeResponse);

        DestinationLikeResponse response = likeService.toggleLike(100L);

        assertNotNull(response);
        assertEquals(100, response.destinationId());
        assertTrue(response.liked());
    }

    @Test
    public void toggleLikeTest_UnauthorizedUser() {
        Mockito.when(userService.getAuthenticatedUser())
                .thenThrow(new RuntimeException("User not authenticated"));

        Assertions.assertThrows(RuntimeException.class, () -> likeService.toggleLike(100L));

        Mockito.verify(destinationService, Mockito.never()).getDestObjById(Mockito.anyLong());
    }

    @Test
    public void getLikeByDestinationId_Success() {
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(userService.getAuthenticatedUser()).thenReturn(testUser);
        Mockito.when(likeRepository.existsByUserAndDestination(testUser, testDestination)).thenReturn(true);
        Mockito.when(likeRepository.countByDestination(testDestination)).thenReturn(1L);
        Mockito.when(likeMapper.toResponse(testDestination, true, 1L)).thenReturn(likeResponse);

        DestinationLikeResponse response = likeService.getLikeByDestinationId(100L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(likeResponse.destinationId(), response.destinationId());
        Assertions.assertTrue(response.liked());
    }

    @Test
    public void getLikeByDestinationId_UnauthorizedUser_ShouldReturnUnliked() {
        Mockito.when(destinationService.getDestObjById(100L)).thenReturn(testDestination);
        Mockito.when(userService.getAuthenticatedUser())
                .thenThrow(new AccessDeniedException("Not authenticated"));
        Mockito.when(likeRepository.countByDestination(testDestination)).thenReturn(1L);
        DestinationLikeResponse likeResponse = Mockito.mock(DestinationLikeResponse.class);
        Mockito.when(likeResponse.destinationId()).thenReturn(testDestination.getId());
        Mockito.when(likeResponse.liked()).thenReturn(false);
        Mockito.when(likeMapper.toResponse(testDestination, false, 1L)).thenReturn(likeResponse);

        DestinationLikeResponse response = likeService.getLikeByDestinationId(100L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(likeResponse.destinationId(), response.destinationId());
        Assertions.assertFalse(response.liked());
    }
}