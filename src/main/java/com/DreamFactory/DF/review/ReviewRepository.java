package com.DreamFactory.DF.review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByUserUsername(String username);
    List<Review> findByDestinationId(Long id);
}
