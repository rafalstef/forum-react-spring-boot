package dev.forum.forum.model.user;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.Post;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, updatable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, updatable = false)
    private String email;

    @Column(name = "created", nullable = false, updatable = false)
    private Instant created;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Post> posts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "subscribers")
    private Set<ForumThread> subscribedThreads;
}