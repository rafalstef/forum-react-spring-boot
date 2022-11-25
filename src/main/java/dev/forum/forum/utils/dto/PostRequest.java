package dev.forum.forum.utils.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long id;
    @Size(min = 16, max = 255)
    private String name;
    @Size(min = 3, max = 32)
    private String forumThreadName;
    @Size(min = 16, max = 1024)
    private String description;
}
