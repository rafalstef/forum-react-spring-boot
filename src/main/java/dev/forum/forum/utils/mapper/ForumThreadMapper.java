package dev.forum.forum.utils.mapper;

import dev.forum.forum.utils.dto.ForumThreadDto;
import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ForumThreadMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(forumThread.getPosts()))")
    @Mapping(target = "numberOfSubscribers", expression = "java(mapSubscribers(forumThread.getSubscribers()))")
    ForumThreadDto mapForumThreadToDto(ForumThread forumThread);

    default Integer mapPosts(List<Post> posts) {
        return posts != null ? posts.size() : 0;
    }

    default Integer mapSubscribers(Set<User> users) {
        return users != null ? users.size() : 0;
    }

    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "subscribers", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    ForumThread mapDtoToForumThread(ForumThreadDto forumThreadDto);
}