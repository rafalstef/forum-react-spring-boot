package dev.forum.forum.utils.mapper;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.repository.CommentRepo;
import dev.forum.forum.repository.VoteRepo;
import dev.forum.forum.service.AuthService;
import dev.forum.forum.utils.dto.PostRequest;
import dev.forum.forum.utils.dto.PostResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostMapperTest {

    @Mock
    AuthService authService;
    @Mock
    VoteRepo voteRepo;
    @InjectMocks
    private PostMapper sut = new PostMapperImpl();
    @Mock
    private CommentRepo commentRepo;

    @Test
    void mapDtoToPost() {
        ForumThread forumThread = ForumThread.builder()
                .name("thread")
                .description("Description of forum thread")
                .build();

        User user = User.builder()
                .id(12L)
                .username("username")
                .build();

        PostRequest postRequest = PostRequest.builder()
                .id(123L)
                .name("Some post name")
                .forumThreadName(forumThread.getName())
                .description(forumThread.getDescription())
                .build();

        Post post = sut.mapDtoToPost(postRequest, forumThread, user);

        assertEquals(postRequest.getId(), post.getId());
        assertEquals(postRequest.getName(), post.getName());
        assertEquals(postRequest.getDescription(), post.getDescription());
        assertEquals(0, post.getVoteCount());
        assertNotNull(post.getCreatedDate());
        assertEquals(user, post.getUser());
        assertEquals(forumThread, post.getForumThread());
    }

    @Test
    void mapToDto() {
        ForumThread forumThread = ForumThread.builder()
                .name("thread")
                .description("Description of forum thread")
                .build();

        User user = User.builder()
                .id(12L)
                .username("username")
                .build();

        Post post = Post.builder()
                .id(123L)
                .name("Some post name")
                .description(forumThread.getDescription())
                .voteCount(100)
                .user(user)
                .createdDate(Instant.now().minus(30, ChronoUnit.DAYS))
                .forumThread(forumThread)
                .build();

        Mockito.when(authService.isLoggedIn()).thenReturn(true);
        Mockito.when(authService.getCurrentUser()).thenReturn(user);
        Mockito.when(voteRepo.findTopByPostAndUserOrderByIdDesc(post, user)).thenReturn(Optional.empty());

        PostResponse postResponse = sut.mapToDto(post);

        assertEquals(post.getId(), postResponse.getId());
        assertEquals(post.getName(), postResponse.getName());
        assertEquals(post.getDescription(), postResponse.getDescription());
        assertEquals(user.getUsername(), postResponse.getUsername());
        assertEquals(forumThread.getName(), postResponse.getForumThreadName());
        assertEquals(post.getVoteCount(), postResponse.getVoteCount());
        assertEquals(0, postResponse.getCommentCount());
        assertNotNull(postResponse.getDuration());
        assertFalse(postResponse.isDownVote());
        assertFalse(postResponse.isUpVote());
    }

    @Test
    void commentCount() {
        Post post = Post.builder()
                .name("post no.1")
                .build();

        Comment comment = Comment.builder()
                .text("Some comment")
                .post(post)
                .build();

        List<Comment> commentList = Collections.singletonList(comment);

        Mockito.when(commentRepo.findByPost(post)).thenReturn(commentList);

        Integer result = sut.commentCount(post);

        Mockito.verify(commentRepo).findByPost(post);
        assertEquals(commentList.size(), result);
    }
}