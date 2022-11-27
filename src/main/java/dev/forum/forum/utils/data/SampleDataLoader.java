package dev.forum.forum.utils.data;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.model.user.UserRole;
import dev.forum.forum.repository.CommentRepo;
import dev.forum.forum.repository.ForumThreadRepo;
import dev.forum.forum.repository.PostRepo;
import dev.forum.forum.repository.UserRepo;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

    private final Faker faker;
    private final UserRepo userRepo;
    private final ForumThreadRepo forumThreadRepo;
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private PasswordEncoder passwordEncoder;

    private User getRandomUserFromList(List<User> list) {
        return list.get(faker.random().nextInt(0, list.size() - 1));
    }

    private Instant randomInstant() {
        return Instant.now().minus(faker.random().nextInt(1, 180), ChronoUnit.DAYS);
    }

    private String getPostTitle(String topic) {
        int random = faker.random().nextInt(0, 1);
        Faker faker = new Faker();
        return switch (topic) {
            case "breaking_bad" -> random == 0 ? faker.breakingBad().episode() : faker.breakingBad().character();
            case "food" -> random == 0 ? faker.food().dish() : faker.food().sushi();
            case "movies" -> random == 0 ? faker.oscarMovie().movieName() : faker.oscarMovie().actor();
            case "basketball" -> random == 0 ? faker.basketball().players() : faker.basketball().teams();
            case "dcComics" -> random == 0 ? faker.dcComics().title() : faker.dcComics().hero();
            case "books" -> random == 0 ? faker.book().title() : faker.book().author();
            default -> faker.lorem().word();
        };
    }

    private Set<User> getRandomSetOfUsers(List<User> list) {
        int size = faker.random().nextInt(1, list.size());
        Set<User> resultSet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            resultSet.add(getRandomUserFromList(list));
        }
        return resultSet;
    }

    @Override
    public void run(String... args) throws Exception {

        User defaultUser = User.builder()
                .username("janek")
                .password(passwordEncoder.encode("password"))
                .email("janek@email.com")
                .created(Instant.now().minus(180, ChronoUnit.DAYS))
                .enabled(true)
                .userRole(UserRole.USER)
                .build();

        List<User> users = new java.util.ArrayList<>(IntStream.rangeClosed(1, 100)
                .mapToObj(i -> User.builder()
                        .username(faker.name().username())
                        .email(faker.internet().emailAddress())
                        .password(passwordEncoder.encode(faker.internet().password()))
                        .created(randomInstant())
                        .enabled(Boolean.TRUE)
                        .userRole(UserRole.USER)
                        .build()
                ).toList());

        users.add(defaultUser);

        List<String> forumNames = Arrays.asList("breaking_bad", "food", "movies", "basketball", "dcComics", "books");

        List<ForumThread> forumThreads = IntStream.rangeClosed(0, forumNames.size() - 1)
                .mapToObj(i -> ForumThread.builder()
                        .name(forumNames.get(i))
                        .description("This is thread about " + forumNames.get(i) + ".")
                        .subscribers(getRandomSetOfUsers(users))
                        .createdDate(randomInstant())
                        .user(i == 0 ? defaultUser : getRandomUserFromList(users))
                        .build())
                .toList();

        List<Post> posts = IntStream.rangeClosed(1, 80)
                .mapToObj(i -> {
                    ForumThread randomThread = forumThreads.get(faker.random().nextInt(0, forumThreads.size() - 1));
                    String postName = getPostTitle(randomThread.getName());
                    return Post.builder()
                            .name(postName)
                            .description("What do you think about " + postName + "?")
                            .user(getRandomUserFromList(users))
                            .forumThread(randomThread)
                            .createdDate(randomThread.getCreatedDate().plus(faker.random().nextInt(0, 7),
                                    ChronoUnit.DAYS))
                            .build();
                })
                .toList();

        List<Comment> comments = IntStream.rangeClosed(1, 160)
                .mapToObj(i -> {
                    Post randomPost = posts.get(faker.random().nextInt(0, posts.size() - 1));
                    return Comment.builder()
                            .text(faker.lorem().sentence(faker.random().nextInt(1, 5)))
                            .user(getRandomUserFromList(users))
                            .post(randomPost)
                            .createdDate(randomPost.getCreatedDate().plus(faker.random().nextInt(0, 7),
                                    ChronoUnit.DAYS))
                            .build();
                })
                .toList();


        userRepo.saveAll(users);
        forumThreadRepo.saveAll(forumThreads);
        postRepo.saveAll(posts);
        commentRepo.saveAll(comments);
    }
}
