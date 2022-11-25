package dev.forum.forum.controller;

import dev.forum.forum.service.UserService;
import dev.forum.forum.utils.dto.UserGetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    @ResponseStatus(OK)
    public UserGetDto getUser(@PathVariable(value = "username") @Size(min = 3, max = 30) String username) {
        return userService.getUserByUsername(username);
    }
}

