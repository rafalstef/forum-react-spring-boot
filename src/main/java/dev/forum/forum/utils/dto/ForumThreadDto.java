package dev.forum.forum.utils.dto;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ForumThreadDto {
    private Long id;

    @Size(min = 3, max = 32, message = "Name must be between 3 and 32 characters long")
    private String name;

    @Size(max = 255, message = "Max length of description is 255 characters")
    private String description;

    private Integer numberOfPosts;

    private Integer numberOfSubscribers;
}
