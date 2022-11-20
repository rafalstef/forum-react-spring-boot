package dev.forum.forum.service;

import dev.forum.forum.dto.UserGetDto;
import dev.forum.forum.exception.ResourceNotFoundException;
import dev.forum.forum.mapper.MapStructMapper;
import dev.forum.forum.model.user.User;
import dev.forum.forum.model.user.UserRole;
import dev.forum.forum.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private MapStructMapper mapper;
    @InjectMocks
    private UserService sut;

    @Test
    void getUserByUsername_ValidUsername_ReturnUserGetDto() {
        String username = "john_doe";
        Instant created = Instant.now();

        User user = User.builder()
                .id(123L)
                .email("john.doe@email.com")
                .password("password")
                .username(username)
                .created(created)
                .enabled(Boolean.TRUE)
                .userRole(UserRole.USER)
                .build();


        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        when(mapper.mapUserToUserGetDto(user)).thenReturn(new UserGetDto(username, created));

        UserGetDto resultUser = sut.getUserByUsername(username);

        assertEquals(user.getUsername(), resultUser.getUsername());
        assertEquals(user.getCreated(), resultUser.getCreated());
    }

    @Test
    void getUserByUsername_InvalidUsername_ThrowsException() {
        String username = "john_doe";
        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getUserByUsername(username)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void loadUserByUsername_ValidUsername_ReturnSecurityUser() {
        String username = "joh_doe";
        User user = User.builder()
                .id(123L)
                .email("john.doe@email.com")
                .password("password")
                .username(username)
                .created(Instant.now())
                .enabled(Boolean.TRUE)
                .userRole(UserRole.USER)
                .build();

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails userDetails = sut.loadUserByUsername(username);

        assertThat(userDetails)
                .isNotNull()
                .hasFieldOrPropertyWithValue("user", user);

        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(user.getUserRole().name())));
    }

    @Test
    void loadUserByUsername_InvalidUsername_ThrowsException() {
        String username = "joh_doe";

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.loadUserByUsername(username)).isInstanceOf(ResourceNotFoundException.class);
    }

}