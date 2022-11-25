package dev.forum.forum.repository;

import dev.forum.forum.model.ForumThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumThreadRepo extends JpaRepository<ForumThread, Long> {
}
