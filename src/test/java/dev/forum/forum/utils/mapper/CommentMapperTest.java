package dev.forum.forum.utils.mapper;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.utils.dto.CommentRequest;
import dev.forum.forum.utils.dto.CommentResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    CommentMapper sut = new CommentMapperImpl();

    @Test
    void mapDtoToComment() {
        User user = User.builder()
                .id(1L)
                .username("john")
                .build();

        Post post = Post.builder()
                .id(100L)
                .build();

        CommentRequest dto = CommentRequest.builder()
                .text("Text")
                .postId(post.getId())
                .build();

        Comment comment = sut.mapDtoToComment(dto, post, user);

        assertNull(comment.getId());
        assertNotNull(comment.getCreatedDate());

        assertEquals(dto.getText(), comment.getText());
        assertEquals(post, comment.getPost());
        assertEquals(user, comment.getUser());
    }

    @Test
    void mapCommentToDto() {
        User user = User.builder()
                .id(1L)
                .username("john")
                .build();

        Post post = Post.builder()
                .id(100L)
                .build();

        Comment comment = Comment.builder()
                .id(12L)
                .createdDate(Instant.now())
                .text("Text")
                .post(post)
                .user(user)
                .build();

        CommentResponse dto = sut.mapCommentToDto(comment);

        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getText(), dto.getText());
        assertEquals(comment.getUser().getUsername(), dto.getUsername());
        assertEquals(comment.getPost().getId(), dto.getPostId());
        assertNotNull(dto.getDuration());
    }
}