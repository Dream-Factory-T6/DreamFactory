package com.DreamFactory.DF.destination;

import com.DreamFactory.DF.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    @Query("SELECT d FROM Destination d WHERE LOWER(d.location) LIKE LOWER(CONCAT('%', :location, '%')) AND LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Destination> findByLocationAndTitleContainingIgnoreCase(@Param("location") String location,
                                                                 @Param("title") String title, Pageable pageable);

    @Query("SELECT d FROM Destination d WHERE LOWER(d.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    Page<Destination> findByLocationContainingIgnoreCase(@Param("location") String location, Pageable pageable);

    @Query("SELECT d FROM Destination d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    Page<Destination> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    Page<Destination> findByUser(User user, Pageable pageable);
}
