package com.DreamFactory.DF.destination.dto;

import com.DreamFactory.DF.destination.Destination;
//import com.DreamFactory.DF.review.ReviewMapper;
import com.DreamFactory.DF.user.dto.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DestinationMapper {

    public static Destination toEntity(DestinationRequest request) {
        if (request == null) {
            return null;
        }

        Destination destination = new Destination();
        destination.setTitle(request.title());
        destination.setLocation(request.location());
        destination.setDescription(request.description());
        destination.setImageUrl(request.imageUrl());

        return destination;
    }

//    public static DestinationResponse toResponse(Destination destination) {
//        if (destination == null)
//            return null;
//        return new DestinationResponse(
//                destination.getId(),
//                destination.getTitle(),
//                destination.getLocation(),
//                destination.getDescription(),
//                destination.getImageUrl(),
//                destination.getUser() != null ? destination.getUser().getUsername() : null,
//                destination.getCreatedAt(),
//                destination.getUpdatedAt(),
//                destination.getReviews() != null ? destination.getReviews().stream()
//                        .map(ReviewMapper::toResponse)
//                        .collect(Collectors.toList()) : List.of());
//    }
}
