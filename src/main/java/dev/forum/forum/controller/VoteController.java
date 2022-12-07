package dev.forum.forum.controller;

import dev.forum.forum.service.VoteService;
import dev.forum.forum.utils.dto.VoteDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void giveVote(@RequestBody @Valid VoteDto voteDto) {
        voteService.vote(voteDto);
    }
}
