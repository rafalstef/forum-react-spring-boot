package dev.forum.forum.utils.mapper;

import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.utils.dto.ForumThreadDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ForumThreadMapperTest {

    ForumThreadMapper sut = new ForumThreadMapperImpl();

    @Test
    void mapForumThreadToDto() {
        List<Post> postList = Collections.singletonList(new Post());
        ForumThread forumThread = ForumThread.builder()
                .id(123L)
                .name("thread")
                .description("Description")
                .posts(postList)
                .createdDate(Instant.now())
                .user(new User())
                .subscribers(Collections.emptySet())
                .build();

        ForumThreadDto dto = sut.mapForumThreadToDto(forumThread);

        assertEquals(forumThread.getId(), dto.getId());
        assertEquals(forumThread.getName(), dto.getName());
        assertEquals(forumThread.getDescription(), dto.getDescription());
        assertEquals(1, dto.getNumberOfPosts());
        assertEquals(0, dto.getNumberOfSubscribers());
    }

    @Test
    void mapPosts() {
        List<Post> postList = new ArrayList<>();
        int size = 10;
        for (int i = 0; i < size; i++) {
            Post post = Post.builder().id((long) i).build();
            postList.add(post);
        }

        assertEquals(size, sut.mapPosts(postList));
    }

    @Test
    void mapSubscribers() {
        int size = 10;
        Set<User> userList = new HashSet<>();
        for (int i = 0; i < size; i++) {
            User post = User.builder().id((long) i).build();
            userList.add(post);
        }

        assertEquals(size, sut.mapSubscribers(userList));
    }

    @Test
    void mapDtoToForumThread() {
        ForumThreadDto dto = ForumThreadDto.builder()
                .id(21L)
                .name("Name")
                .description("Description")
                .numberOfPosts(120)
                .numberOfSubscribers(10000)
                .build();

        ForumThread forumThread = sut.mapDtoToForumThread(dto);

        assertEquals(dto.getId(), forumThread.getId());
        assertEquals(dto.getName(), forumThread.getName());
        assertEquals(dto.getDescription(), forumThread.getDescription());
        assertNull(forumThread.getPosts());
        assertNull(forumThread.getSubscribers());
    }
}