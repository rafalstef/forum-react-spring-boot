package dev.forum.forum.repository;

import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findAllByForumThreadOrderByCreatedDateDesc(ForumThread forumThread);

    List<Post> findAllByUserOrderByCreatedDateDesc(User user);

    List<Post> findAllByForumThreadInOrderByCreatedDateDesc(Set<ForumThread> subscribedThreads);
}
