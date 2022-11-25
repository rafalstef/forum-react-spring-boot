package dev.forum.forum.service;

import dev.forum.forum.utils.dto.AuthenticationResponse;
import dev.forum.forum.utils.dto.LoginRequest;
import dev.forum.forum.utils.dto.RegisterRequest;
import dev.forum.forum.utils.email.EmailDetails;
import dev.forum.forum.utils.email.EmailService;
import dev.forum.forum.utils.exception.ResourceNotFoundException;
import dev.forum.forum.model.ConfirmationToken;
import dev.forum.forum.model.user.SecurityUser;
import dev.forum.forum.model.user.User;
import dev.forum.forum.model.user.UserRole;
import dev.forum.forum.repository.ConfirmationTokenRepo;
import dev.forum.forum.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final EmailService emailService;

    public void signup(RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .created(Instant.now())
                .userRole(UserRole.USER)
                .enabled(Boolean.FALSE)
                .build();

        userRepo.save(user);

        // create confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        // save to database
        confirmationTokenRepo.save(confirmationToken);

        // send conformation mail with token
        emailService.sendSimpleMail(
                new EmailDetails(
                        user.getEmail(),
                        "Hello " + user.getUsername() + "!\n" +
                                "Please click on the below url to activate your account : " +
                                "http://localhost:8080/verify-account/" + token,
                        "Verify your email address",
                        null
                )
        );
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        return AuthenticationResponse.builder()
                .id(securityUser.user().getId())
                .username(securityUser.getUsername())
                .build();
    }

    public void verifyAccount(String token) {
        ConfirmationToken confirmationToken =
                confirmationTokenRepo.findByToken(token)
                        .orElseThrow(() -> new ResourceNotFoundException("Token: " + token + " not found."));

        String username = confirmationToken.getUser().getUsername();

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found."));

        // if found set enabled
        user.setEnabled(Boolean.TRUE);
        userRepo.save(user);
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found - " + auth.getName()));
    }
}




