package dev.forum.forum.utils.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import dev.forum.forum.utils.dto.PostRequest;
import dev.forum.forum.utils.dto.PostResponse;
import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.Vote;
import dev.forum.forum.model.VoteType;
import dev.forum.forum.model.user.User;
import dev.forum.forum.repository.CommentRepo;
import dev.forum.forum.repository.VoteRepo;
import dev.forum.forum.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static dev.forum.forum.model.VoteType.DOWN_VOTE;
import static dev.forum.forum.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private VoteRepo voteRepo;
    @Autowired
    private AuthService authService;

    @Mapping(target = "id", source = "postRequest.id")
    @Mapping(target = "name", source = "postRequest.name")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "forumThread", source = "forumThread")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post mapDtoToPost(PostRequest postRequest, ForumThread forumThread, User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "forumThreadName", source = "forumThread.name")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepo.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWN_VOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepo.findTopByPostAndUserOrderByIdDesc(post, authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType)).isPresent();
        }
        return false;
    }

}
