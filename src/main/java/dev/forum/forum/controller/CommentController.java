package dev.forum.forum.controller;

import dev.forum.forum.service.CommentService;
import dev.forum.forum.utils.dto.CommentRequest;
import dev.forum.forum.utils.dto.CommentResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/user/{username}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsCreatedByUser(@PathVariable(value = "username") String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllByUser(username));
    }

    @GetMapping("post/{postId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsUnderPost(@PathVariable(value = "postId") Long postId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getAllByPost(postId));
    }

    @PostMapping()
    public ResponseEntity<CommentResponse> createNewComment(@Valid @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.create(commentRequest));
    }
}
