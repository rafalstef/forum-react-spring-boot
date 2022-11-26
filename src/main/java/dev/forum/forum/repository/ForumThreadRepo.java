package dev.forum.forum.repository;

import dev.forum.forum.model.ForumThread;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ForumThreadRepo extends JpaRepository<ForumThread, Long> {

    Optional<ForumThread> findByName(String name);

    @Query(value = "SELECT f FROM ForumThread f ORDER BY f.subscribers.size DESC")
    List<ForumThread> findAllSortedBySubscribersSize(Pageable pageable);

}
