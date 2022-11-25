package dev.forum.forum.utils.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;

    @NotNull("Post id cannot be null")
    private Long postId;

    private Instant createdDate;

    @Size(min = 3, max = 512, message = "Comment length must be between 3 and 512 characters long.")
    private String text;

    @NotNull("Username cannot be null")
    private String username;
}
