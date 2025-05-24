package it.epicode.Circle.posts;

import it.epicode.Circle.comments.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long id;
    private String content;
    private String mediaUrl;
    private String createdAt;
    private Long authorId;
    private String authorFullName;
    private List<CommentResponse> comments = new ArrayList<>();
    private List<Long> likeIds = new ArrayList<>();
}
