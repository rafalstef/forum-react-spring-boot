package dev.forum.forum.controller;

import dev.forum.forum.utils.dto.AuthenticationResponse;
import dev.forum.forum.utils.dto.LoginRequest;
import dev.forum.forum.utils.dto.RegisterRequest;
import dev.forum.forum.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public void createNewUserAccount(@RequestBody @Valid RegisterRequest registerRequest) {
        authService.signup(registerRequest);
    }

    @GetMapping("/verify-account/{token}")
    public String verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return "Account activated successfully";
    }

}
