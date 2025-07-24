package com.DreamFactory.DF.like;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.destination.DestinationService;
import com.DreamFactory.DF.like.dtos.DestinationLikeMapper;
import com.DreamFactory.DF.like.dtos.DestinationLikeResponse;
import com.DreamFactory.DF.user.UserService;
import com.DreamFactory.DF.user.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinationLikeService {
    private final DestinationLikeRepository likeRepository;
    private final DestinationService destinationService;
    private final UserService userService;
    private final DestinationLikeMapper mapper;

    @Transactional
    public DestinationLikeResponse toggleLike(Long destinationId) {
        User user = userService.getAuthenticatedUser();
        Destination destination = destinationService.getDestObjById(destinationId);

        Optional<DestinationLike> optionalLike = likeRepository.findByUserAndDestination(user, destination);

        if (optionalLike.isPresent()) {
            likeRepository.delete(optionalLike.get());
        } else {
            DestinationLike like = DestinationLike.builder()
                    .destination(destination)
                    .user(user)
                    .build();
            likeRepository.save(like);
        }
        boolean liked = likeRepository.existsByUserAndDestination(user, destination);
        long count = likeRepository.countByDestination(destination);

        return mapper.toResponse(destination, liked, count);
    }

    @Transactional
    public DestinationLikeResponse getLikeByDestinationId(Long destinationId) {
        Destination destination = destinationService.getDestObjById(destinationId);
        boolean liked;

        try {
            User user = userService.getAuthenticatedUser();
            liked = likeRepository.existsByUserAndDestination(user, destination);
        } catch (AccessDeniedException | AuthenticationException e) {
            liked = false;
        }
        long count = likeRepository.countByDestination(destination);

        return mapper.toResponse(destination, liked, count);
    }
}
