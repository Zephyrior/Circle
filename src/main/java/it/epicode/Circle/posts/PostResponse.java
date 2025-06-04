package it.epicode.Circle.posts;

import it.epicode.Circle.comments.CommentResponse;
import it.epicode.Circle.likes.LikeResponse;
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
    private String authorProfilePictureUrl;
    private List<CommentResponse> comments = new ArrayList<>();
    private List<LikeResponse> likes = new ArrayList<>();
    private int likesCount;
    private boolean likedByUser;
    private Long profileOwnerId;
    private String profileOwnerFullName;
}
