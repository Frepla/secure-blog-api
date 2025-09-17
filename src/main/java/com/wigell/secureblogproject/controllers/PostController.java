package com.wigell.secureblogproject.controllers;

import com.wigell.secureblogproject.dto.PostUpdateRequest;
import com.wigell.secureblogproject.entities.Post;
import com.wigell.secureblogproject.services.PostService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v2/")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        if (posts.isEmpty()) {
            return ok("There are no posts in the database");
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/newpost")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @AuthenticationPrincipal Jwt principal) {
        Post createdPost = postService.createPost(post, principal);
        return ResponseEntity.status(201).body(createdPost);
    }

    @PutMapping("/updatepost")
    public ResponseEntity<Post> updatePost(@RequestBody PostUpdateRequest request, @AuthenticationPrincipal Jwt principal) {
        Post updatedPost = postService.updatePost(request, principal);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/deletepost/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable Long id, @AuthenticationPrincipal Jwt principal) {
        String message = postService.deletePostById(id, principal);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/count")
    public ResponseEntity<String> getPostCount() {
        return ResponseEntity.ok(postService.countPosts());
    }
}
