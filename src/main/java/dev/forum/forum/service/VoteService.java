package dev.forum.forum.service;

import dev.forum.forum.model.Post;
import dev.forum.forum.model.Vote;
import dev.forum.forum.model.user.User;
import dev.forum.forum.repository.PostRepo;
import dev.forum.forum.repository.VoteRepo;
import dev.forum.forum.utils.dto.VoteDto;
import dev.forum.forum.utils.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static dev.forum.forum.model.VoteType.DOWNVOTE;
import static dev.forum.forum.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepo voteRepo;
    private final PostRepo postRepo;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepo.findById(voteDto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + voteDto.getPostId() + " not found" + "."));

        User currentUser = authService.getCurrentUser();
        Optional<Vote> voteByPostAndUser = voteRepo.findTopByPostAndUserOrderByIdDesc(post, currentUser);

        // if empty, create new vote and save
        if (voteByPostAndUser.isEmpty()) {
            Vote voteToSave = Vote.builder()
                    .voteType(voteDto.getVoteType())
                    .post(post)
                    .user(currentUser)
                    .build();

            voteRepo.save(voteToSave);
            return;
        }

        // check type of vote from the database
        if (voteByPostAndUser.get().getVoteType().equals(UPVOTE)) {
            if (voteDto.getVoteType().equals(UPVOTE)) {
                // db: UPVOTE, request: UPVOTE
                voteRepo.delete(voteByPostAndUser.get());
            } else {
                // db: UPVOTE, request: DOWNVOTE
                voteByPostAndUser.get().setVoteType(DOWNVOTE);
                voteRepo.save(voteByPostAndUser.get());
            }
        } else {
            if (voteDto.getVoteType().equals(DOWNVOTE)) {
                // db: DOWNVOTE, request: DOWNVOTE
                voteRepo.delete(voteByPostAndUser.get());
            } else {
                // db: DOWNVOTE, request: UPVOTE
                voteByPostAndUser.get().setVoteType(UPVOTE);
                voteRepo.save(voteByPostAndUser.get());
            }
        }
    }
}
