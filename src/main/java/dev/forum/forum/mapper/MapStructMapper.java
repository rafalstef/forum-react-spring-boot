package dev.forum.forum.mapper;

import dev.forum.forum.dto.UserGetDto;
import dev.forum.forum.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

    UserGetDto mapUserToUserGetDto(User user);

}
