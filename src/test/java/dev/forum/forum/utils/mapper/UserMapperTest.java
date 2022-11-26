package dev.forum.forum.utils.mapper;

import dev.forum.forum.model.user.User;
import dev.forum.forum.utils.dto.UserGetDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper sut = new UserMapperImpl();

    @Test
    void mapUserToUserGetDto() {
        User user = User.builder()
                .id(123L)
                .username("username")
                .password("password")
                .created(Instant.now())
                .email("user@email.com")
                .build();

        UserGetDto dto = sut.mapUserToUserGetDto(user);

        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getCreated(), dto.getCreated());
    }
}