package dev.forum.forum.utils.dto;

import dev.forum.forum.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    @NotNull("Vote type cannot be null")
    private VoteType voteType;
    @NotNull("Post id cannot be null")
    private Long postId;
}
