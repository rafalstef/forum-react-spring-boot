package dev.forum.forum.model;

import dev.forum.forum.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class ForumThread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, updatable = false, length = 32)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "forumThread")
    private List<Post> posts;

    @Column(name = "created_date", updatable = false)
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "thread_subscribers",
            joinColumns = @JoinColumn(name = "thread_id", updatable = false, nullable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", updatable = false, nullable = false)
    )
    private Set<User> subscribers;

}
