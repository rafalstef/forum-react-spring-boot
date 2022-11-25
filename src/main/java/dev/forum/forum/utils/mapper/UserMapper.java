package dev.forum.forum.utils.mapper;

import dev.forum.forum.utils.dto.UserGetDto;
import dev.forum.forum.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserGetDto mapUserToUserGetDto(User user);

}
