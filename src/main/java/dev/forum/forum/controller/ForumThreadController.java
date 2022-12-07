package dev.forum.forum.controller;

import dev.forum.forum.service.ForumThreadService;
import dev.forum.forum.utils.dto.ForumThreadDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/threads")
public class ForumThreadController {

    private final ForumThreadService forumThreadService;

    @GetMapping()
    public ResponseEntity<List<ForumThreadDto>> getMostPopular() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(forumThreadService.getAllSortedByNumberOfSubscribers());
    }

    @GetMapping("/top")
    public ResponseEntity<List<ForumThreadDto>> getFirstFiveMostPopular() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(forumThreadService.getTopFiveSortedBySubscribers());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ForumThreadDto> getForumThreadByName(@PathVariable(value = "name") String forumThreadName) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(forumThreadService.getByName(forumThreadName));
    }

    @PostMapping()
    public ResponseEntity<ForumThreadDto> createForumThread(@RequestBody @Valid ForumThreadDto forumThreadDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(forumThreadService.createForumThread(forumThreadDto));
    }

    @PutMapping("/{name}")
    public ResponseEntity<?> updateDescription(@PathVariable(value = "name") String name,
                                               @RequestBody @Valid ForumThreadDto forumThreadDto) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(forumThreadService.updateDescription(name, forumThreadDto.getDescription()));
        } catch (AuthorizationServiceException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }
}
