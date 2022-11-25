package dev.forum.forum.repository;

import dev.forum.forum.model.Post;
import dev.forum.forum.model.Vote;
import dev.forum.forum.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepo extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);
}
