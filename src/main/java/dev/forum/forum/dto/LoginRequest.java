package dev.forum.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username cannot be empty")
    String username;
    @NotBlank(message = "Password cannot be empty")
    String password;
}
