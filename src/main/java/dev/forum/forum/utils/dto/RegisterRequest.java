package dev.forum.forum.utils.dto;

import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {

    @Size(min = 3, max = 30, message = "The username cannot be less than 3 and longer than 30 characters long.")
    private String username;

    @NotNull(message = "Email is required.")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid email address format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be minimum eight characters and contain at least one letter and one number."
    )
    private String password;
}
