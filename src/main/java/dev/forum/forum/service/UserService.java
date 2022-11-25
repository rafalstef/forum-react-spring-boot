package dev.forum.forum.service;

import dev.forum.forum.utils.dto.UserGetDto;
import dev.forum.forum.utils.exception.ResourceNotFoundException;
import dev.forum.forum.utils.mapper.UserMapper;
import dev.forum.forum.model.user.SecurityUser;
import dev.forum.forum.model.user.User;
import dev.forum.forum.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found in the database."));
        return new SecurityUser(user);
    }

    public UserGetDto getUserByUsername(String username) {
        return mapper.mapUserToUserGetDto(userRepo.findByUsername(username)
                .filter(user -> user.isEnabled() == Boolean.TRUE)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found."))
        );
    }
}
