package com.wigell.secureblogproject.repositories;

import com.wigell.secureblogproject.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
