package com.wigell.secureblogproject.controllers;

import com.wigell.secureblogproject.dto.PostUpdateRequest;
import com.wigell.secureblogproject.entities.Author;
import com.wigell.secureblogproject.entities.Post;
import com.wigell.secureblogproject.repositories.AuthorRepository;
import com.wigell.secureblogproject.repositories.PostRepository;
import com.wigell.secureblogproject.services.PostService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v2/")
public class PostController {
    private final PostService postService;
    private final AuthorRepository authorRepository;

    public PostController(PostService postService, AuthorRepository authorRepository) {
        this.postService = postService;
        this.authorRepository = authorRepository;
    }

    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        if (posts.isEmpty()) {
            return ok("There are no posts in the database");
        }
        return ok(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ok(post);
    }

    @PostMapping("/newpost")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @AuthenticationPrincipal Jwt principal) {
        return ok(postService.createPost(post, principal));
    }

    @PutMapping("/updatepost")
    public ResponseEntity<Post> updatePost(@RequestBody PostUpdateRequest request, @AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("preferred_username");

        Author author = authorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Author not found for username: " + username));

        Post updatedPost = postService.updatePost(request, author);
        return ok(updatedPost);
    }

    @DeleteMapping("/deletepost/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable Long id, @AuthenticationPrincipal Jwt principal) {
        String message = postService.deletePostById(id, principal);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/count")
    public String getPostCount() {
        return postService.countPosts();
    }
}
