package dev.forum.forum.utils.data;

import dev.forum.forum.model.*;
import dev.forum.forum.model.user.User;
import dev.forum.forum.model.user.UserRole;
import dev.forum.forum.repository.*;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class SampleDataLoader implements CommandLineRunner {

    private static final List<String> forumNames = Arrays.asList("breaking_bad", "food", "movies", "basketball",
            "dcComics", "books");
    private final Faker faker;
    private final UserRepo userRepo;
    private final ForumThreadRepo forumThreadRepo;
    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final VoteRepo voteRepo;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        List<User> fakeUsers = generateFakeUsers();
        List<ForumThread> fakeForumThreads = generateFakeForumThreads(fakeUsers);
        List<Post> fakePosts = generateFakePosts(fakeForumThreads, fakeUsers);
        List<Comment> fakeComments = generateFakeComments(fakePosts, fakeUsers);
        List<Vote> fakeVotes = generateFakeVotes(fakePosts, fakeUsers);

        userRepo.saveAll(fakeUsers);
        forumThreadRepo.saveAll(fakeForumThreads);
        postRepo.saveAll(fakePosts);
        commentRepo.saveAll(fakeComments);
        voteRepo.saveAll(fakeVotes);
    }

    private List<User> generateFakeUsers() {
        User defaultUser = User.builder()
                .username("janek")
                .password(passwordEncoder.encode("password"))
                .email("janek@email.com")
                .created(Instant.now().minus(180, ChronoUnit.DAYS))
                .enabled(true)
                .userRole(UserRole.USER)
                .build();

        List<User> users = new java.util.ArrayList<>(IntStream.rangeClosed(1, 100 - 1)
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

        return users;
    }

    private List<ForumThread> generateFakeForumThreads(List<User> users) {
        return IntStream.rangeClosed(0, forumNames.size() - 1)
                .mapToObj(i -> ForumThread.builder()
                        .name(forumNames.get(i))
                        .description("This is thread about " + forumNames.get(i) + ".")
                        .subscribers(getRandomSetOfUsers(users))
                        .createdDate(randomInstant())
                        .user(getRandomUserFromList(users))
                        .build())
                .toList();
    }

    private List<Post> generateFakePosts(List<ForumThread> forumThreads, List<User> users) {
        return IntStream.rangeClosed(1, 80)
                .mapToObj(i -> {
                    ForumThread randomThread = forumThreads.get(faker.random().nextInt(0,
                            forumThreads.size() - 1));
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
    }

    private List<Comment> generateFakeComments(List<Post> posts, List<User> users) {
        return IntStream.rangeClosed(1, 160)
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
    }

    private List<Vote> generateFakeVotes(List<Post> posts, List<User> users) {
        List<Vote> votes = new ArrayList<>();

        for (Post post : posts) {
            Set<User> userSubList = getRandomSetOfUsers(users);
            for (User user : userSubList) {
                int x = faker.random().nextInt(0, 100);
                Vote vote = Vote.builder()
                        .voteType(x <= 80 ? VoteType.UPVOTE : VoteType.DOWNVOTE)
                        .post(post)
                        .user(user)
                        .build();

                votes.add(vote);
            }
        }

        return votes;
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

    private User getRandomUserFromList(List<User> list) {
        return list.get(faker.random().nextInt(0, list.size() - 1));
    }

    private Instant randomInstant() {
        return Instant.now().minus(faker.random().nextInt(1, 90), ChronoUnit.DAYS);
    }
}
