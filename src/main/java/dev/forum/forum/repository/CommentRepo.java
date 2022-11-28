package dev.forum.forum.repository;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUserOrderByCreatedDateDesc(User user);

    List<Comment> findAllByPostOrderByCreatedDateDesc(Post post);

    Integer countByPost(Post post);
}
