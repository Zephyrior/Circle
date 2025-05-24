package it.epicode.Circle.comments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private String createdAt;
    private Long authorId;
    private String authorFullName;
    private Long postId;
}
