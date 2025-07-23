package com.DreamFactory.DF.like;

import com.DreamFactory.DF.destination.Destination;
import com.DreamFactory.DF.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinationLikeRepository extends JpaRepository<DestinationLike, Long> {
    Optional<DestinationLike> findByUserAndDestination(User user, Destination destination);

    boolean existsByUserAndDestination(User user, Destination destination);

    long countByDestination(Destination destination);
}
