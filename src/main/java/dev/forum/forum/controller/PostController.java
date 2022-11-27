package dev.forum.forum.controller;

import dev.forum.forum.service.PostService;
import dev.forum.forum.utils.dto.PostRequest;
import dev.forum.forum.utils.dto.PostResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/thread/{forumThreadName}")
    public ResponseEntity<List<PostResponse>> getAllPostsFromThread(@PathVariable("forumThreadName") String forumThreadName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllByForumThread(forumThreadName));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsCreatedByUser(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllByUser(username));
    }

    @GetMapping("/subscribed/{username}")
    public ResponseEntity<List<PostResponse>> getPostsFromSubscribedThreads(@PathVariable("username") String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getLastPostsFromSubscribedThreads(username));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getAllPostsFromThread(@PathVariable("postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getOneByPostId(postId));
    }

    @PostMapping()
    public ResponseEntity<PostResponse> createPost(@RequestBody @Valid PostRequest postRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.create(postRequest));
    }


}
