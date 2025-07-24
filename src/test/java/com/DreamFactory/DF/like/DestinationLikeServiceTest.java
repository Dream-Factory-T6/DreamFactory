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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
        likeService = Mockito.spy(new DestinationLikeService(likeRepository,
                destinationService,
                userService,
                likeMapper));

        testUser =new User();
        testUser.setUsername("testUser");
        testUser.setId(1L);

        testDestination = new Destination();
        testDestination.setId(100L);
        testDestination.setTitle("Test Destination");
        testDestination.setLocation("Test Location");
        testDestination.setDescription("Test Description");

        likeResponse = new DestinationLikeResponse(100L, true, 1);
    }

    @Test
    public void toggleLikeTest () {
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

        Assertions.assertThrows(RuntimeException.class, () -> {
            likeService.toggleLike(100L);
        });

        Mockito.verify(destinationService, Mockito.never()).getDestObjById(Mockito.anyLong());
    }
}