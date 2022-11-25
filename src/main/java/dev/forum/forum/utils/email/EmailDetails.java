package dev.forum.forum.utils.email;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}