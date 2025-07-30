package com.DreamFactory.DF.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.roomId = :roomId ORDER BY m.createdAt DESC")
    List<Message> findMessagesByRoomIdOrderByCreatedAtDesc(@Param("roomId") String roomId);

    @Query("SELECT m FROM Message m WHERE m.roomId = :roomId ORDER BY m.createdAt ASC")
    List<Message> findMessagesByRoomIdOrderByCreatedAtAsc(@Param("roomId") String roomId);

    @Query("SELECT m FROM Message m WHERE m.roomId = :roomId ORDER BY m.createdAt DESC LIMIT :limit")
    List<Message> findRecentMessagesByRoomId(@Param("roomId") String roomId, @Param("limit") int limit);
}