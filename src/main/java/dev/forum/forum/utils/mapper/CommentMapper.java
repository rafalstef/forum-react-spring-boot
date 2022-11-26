package dev.forum.forum.utils.mapper;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.utils.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    Comment mapDtoToComment(CommentDto commentDto, Post post, User user);

    @Mapping(target = "postId", source = "comment.post.id")
    @Mapping(target = "username", source = "comment.user.username")
    CommentDto mapCommentToDto(Comment comment);

}
