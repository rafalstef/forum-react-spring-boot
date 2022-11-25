package dev.forum.forum.repository;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
