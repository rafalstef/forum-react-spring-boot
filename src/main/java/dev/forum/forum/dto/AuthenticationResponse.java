package dev.forum.forum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private Long id;
    private String username;
}
