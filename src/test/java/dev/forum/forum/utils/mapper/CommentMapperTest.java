package dev.forum.forum.utils.mapper;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.utils.dto.CommentDto;
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

        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("Text")
                .postId(post.getId())
                .username(user.getUsername())
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

        CommentDto dto = sut.mapCommentToDto(comment);

        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getText(), dto.getText());
        assertEquals(comment.getCreatedDate(), dto.getCreatedDate());
        assertEquals(comment.getUser().getUsername(), dto.getUsername());
        assertEquals(comment.getPost().getId(), dto.getPostId());
    }
}