package com.wigell.secureblogproject.services;

import com.wigell.secureblogproject.dto.PostUpdateRequest;
import com.wigell.secureblogproject.entities.Post;
import com.wigell.secureblogproject.entities.Author;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface PostService {
    List<Post> getAllPosts();

    Post getPostById(Long id);

    Post updatePost(PostUpdateRequest request, Author author);

    Post createPost(Post post, Jwt principal);

    String deletePostById(Long postId, Jwt principal);

    String countPosts();
}
