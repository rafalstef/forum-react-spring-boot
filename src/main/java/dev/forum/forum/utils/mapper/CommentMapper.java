package dev.forum.forum.utils.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import dev.forum.forum.model.Comment;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.utils.dto.CommentRequest;
import dev.forum.forum.utils.dto.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "text", source = "commentRequest.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Comment mapDtoToComment(CommentRequest commentRequest, Post post, User user);


    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "text", source = "comment.text")
    @Mapping(target = "postId", source = "comment.post.id")
    @Mapping(target = "username", source = "comment.user.username")
    @Mapping(target = "duration", expression = "java(getDuration(comment))")
    CommentResponse mapCommentToDto(Comment comment);

    default String getDuration(Comment comment) {
        return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
    }
}
