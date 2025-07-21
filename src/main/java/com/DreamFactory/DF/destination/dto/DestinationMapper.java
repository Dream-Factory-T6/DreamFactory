package com.DreamFactory.DF.destination.dto;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.review.dtos.ReviewMapper;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DestinationMapper {

    public static Destination toEntity(DestinationRequest request, String imageUrl) {
        if (request == null) {
            return null;
        }

        Destination destination = new Destination();
        destination.setTitle(request.title());
        destination.setLocation(request.location());
        destination.setDescription(request.description());
        destination.setImageUrl(imageUrl);

        return destination;
    }

    public static DestinationResponse toResponse(Destination destination) {
        if (destination == null)
            return null;
        Double rating = calculateAverageRating(destination);
        return new DestinationResponse(
                destination.getId(),
                destination.getTitle(),
                destination.getLocation(),
                destination.getDescription(),
                destination.getImageUrl(),
                destination.getUser() != null ? destination.getUser().getUsername() : null,
                rating,
                destination.getCreatedAt(),
                destination.getUpdatedAt()
                );
    }

    public static DestinationWithReviewsResponse toWithReviewsResponse(Destination destination) {
        if (destination == null)
            return null;
        Double rating = calculateAverageRating(destination);
        return new DestinationWithReviewsResponse(
                destination.getId(),
                destination.getTitle(),
                destination.getLocation(),
                destination.getDescription(),
                destination.getImageUrl(),
                destination.getUser() != null ? destination.getUser().getUsername() : null,
                destination.getReviews() != null ? destination.getReviews().stream()
                        .map(ReviewMapper::toReviewResponse)
                        .toList() : List.of(),
                rating,
                destination.getCreatedAt(),
                destination.getUpdatedAt());
    }

    private static Double calculateAverageRating(Destination destination) {
        if (destination.getReviews() == null || destination.getReviews().isEmpty()) {
            return null;
        }
        double avg = destination.getReviews().stream()
                .mapToDouble(r -> r.getRating())
                .average()
                .orElse(Double.NaN);
        if (Double.isNaN(avg)) {
            return null;
        }
        return Math.round(avg * 10.0) / 10.0;
    }
}
