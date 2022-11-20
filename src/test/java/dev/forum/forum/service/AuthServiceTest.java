package dev.forum.forum.service;

import dev.forum.forum.dto.RegisterRequest;
import dev.forum.forum.email.EmailDetails;
import dev.forum.forum.email.EmailService;
import dev.forum.forum.exception.ResourceNotFoundException;
import dev.forum.forum.model.ConfirmationToken;
import dev.forum.forum.model.user.User;
import dev.forum.forum.model.user.UserRole;
import dev.forum.forum.repository.ConfirmationTokenRepo;
import dev.forum.forum.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ConfirmationTokenRepo confirmationTokenRepo;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private AuthService sut;

    @Test
    void signup() {
        String username = "john_doe";
        String email = "john.doe@email.com";
        String password = "password";

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();

        when(passwordEncoder.encode(password)).thenReturn(password);

        sut.signup(registerRequest);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo, times(1)).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertEquals(username, capturedUser.getUsername());
        assertEquals(email, capturedUser.getEmail());
        assertEquals(password, capturedUser.getPassword());

        ArgumentCaptor<ConfirmationToken> confirmationTokenCaptor = ArgumentCaptor.forClass(ConfirmationToken.class);
        verify(confirmationTokenRepo, times(1)).save(confirmationTokenCaptor.capture());
        ConfirmationToken capturedConfirmationToken = confirmationTokenCaptor.getValue();

        assertEquals(capturedUser, capturedConfirmationToken.getUser());

        ArgumentCaptor<EmailDetails> emailCaptor = ArgumentCaptor.forClass(EmailDetails.class);
        verify(emailService, times(1)).sendSimpleMail(emailCaptor.capture());
        EmailDetails capturedEmail = emailCaptor.getValue();

        assertEquals(email, capturedEmail.getRecipient());
    }

    @Test
    void verifyAccount_ValidToken_EnablesUser() {
        User user = User.builder()
                .id(123L)
                .email("john.doe@email.com")
                .password("password")
                .username("john_doe")
                .created(Instant.now())
                .enabled(Boolean.FALSE)
                .userRole(UserRole.USER)
                .build();

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();

        when(confirmationTokenRepo.findByToken(token)).thenReturn(Optional.of(confirmationToken));
        when(userRepo.findByUsername(confirmationToken.getUser().getUsername())).thenReturn(Optional.of(user));

        sut.verifyAccount(token);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo, times(1)).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();

        assertTrue(capturedUser.isEnabled());
    }

    @Test
    void verifyAccount_InvalidToken_ThrowsError() {
        String token = UUID.randomUUID().toString();
        when(confirmationTokenRepo.findByToken(token)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> sut.verifyAccount(token)).isInstanceOf(ResourceNotFoundException.class);
    }
}