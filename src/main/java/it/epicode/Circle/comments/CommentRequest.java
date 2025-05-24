package it.epicode.Circle.comments;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @NotNull(message = "postId is required")
    private Long postId;

    @NotBlank(message = "Content is required")
    private String content;
}
