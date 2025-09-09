package com.wigell.secureblogproject.repositories;

import com.wigell.secureblogproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
