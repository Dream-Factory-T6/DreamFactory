package com.DreamFactory.DF.user;

import com.DreamFactory.DF.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
}
