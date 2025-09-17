package com.wigell.secureblogproject.services;

import com.wigell.secureblogproject.dto.PostUpdateRequest;
import com.wigell.secureblogproject.exceptions.InvalidPostException;
import com.wigell.secureblogproject.exceptions.ResourceNotFoundException;
import com.wigell.secureblogproject.entities.Post;
import com.wigell.secureblogproject.entities.Author;
import com.wigell.secureblogproject.exceptions.UnauthorizedActionException;
import com.wigell.secureblogproject.repositories.AuthorRepository;
import com.wigell.secureblogproject.repositories.PostRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PostServiceIMPL implements PostService {

    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;

    private static final Pattern VALID_TEXT = Pattern.compile("^[a-zA-Z0-9\\s.,!?åäöÅÄÖ-]+$");

    public PostServiceIMPL(AuthorRepository authorRepository, PostRepository postRepository) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    @Override
    public Post createPost(Post post, Jwt principal) {
        String username = principal.getClaim("preferred_username");

        Author author = authorRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "username", username));

        validatePost(post.getTitle(), post.getContent());

        LocalDateTime now = LocalDateTime.now();
        post.setAuthor(author);
        post.setCreatedAt(now);
        post.setUpdatedAt(now);

        Post savedPost = postRepository.save(post);
        System.out.println("New post by user with sub: " + principal.getClaim("sub"));

        return savedPost;
    }

    @Override
    public Post updatePost(PostUpdateRequest request, Jwt principal) {
        String username = principal.getClaim("preferred_username");

        Author author = authorRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "username", username));

        Post existingPost = postRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", request.getId()));

        if (!isAdminOrOwner(existingPost, author)) {
            throw new UnauthorizedActionException("post", "update");
        }

        validatePost(request.getTitle(), request.getContent());

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(existingPost);
    }

    @Override
    public String deletePostById(Long postId, Jwt principal) {
        String username = principal.getClaim("preferred_username");

        Author author = authorRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Author", "username", username));

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (!isAdminOrOwner(existingPost, author)) {
            throw new UnauthorizedActionException("post", "delete");
        }

        postRepository.delete(existingPost);
        return "Post with id " + postId + " has been deleted successfully";
    }

    @Override
    public String countPosts() {
        long count = postRepository.count();
        return "Number of current posts: " + count;
    }

    private boolean isAdminOrOwner(Post post, Author author) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_blogclient_ADMIN"));
        boolean isOwner = post.getAuthor() != null && post.getAuthor().getId().equals(author.getId());
        return isAdmin || isOwner;
    }

    private void validatePost(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new InvalidPostException("Post", "title", title);
        }
        if (!VALID_TEXT.matcher(title).matches()) {
            throw new InvalidPostException("Post", "title", title);
        }
        if (content == null || content.isBlank()) {
            throw new InvalidPostException("Post", "content", content);
        }
        if (!VALID_TEXT.matcher(content).matches()) {
            throw new InvalidPostException("Post", "content", content);
        }
    }
}