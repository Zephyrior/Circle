package it.epicode.Circle.posts;

import it.epicode.Circle.comments.CommentMapper;
import it.epicode.Circle.comments.CommentResponse;
import it.epicode.Circle.likes.LikeResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static PostResponse toResponse(Post post, Long currentUserId) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setMediaUrl(post.getMediaUrl());
        response.setCreatedAt(post.getCreatedAt().format(formatter));
        response.setAuthorId(post.getAuthor().getId());
        response.setAuthorFullName(post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName());
        response.setAuthorProfilePictureUrl(post.getAuthor().getProfilePictureUrl());

        List<CommentResponse> commentResponses = post.getComments()
                .stream()
                .map(CommentMapper::toResponse)
                .collect(Collectors.toList());

        response.setComments(commentResponses);

        response.setLikesCount(post.getLikes().size());

        boolean likedByUser = post.getLikes().stream()
                .anyMatch(like -> like.getUser().getId().equals(currentUserId));

        response.setLikedByUser(likedByUser);

        List<LikeResponse> likeResponses = post.getLikes()
                .stream()
                .filter(like -> like.getUser() != null)
                .map(like -> new LikeResponse(
                        like.getId(),
                        like.getUser().getId(),
                        like.getUser().getFirstName() + " " + like.getUser().getLastName(),
                        like.getUser().getProfilePictureUrl()))
                .toList();

        response.setLikes(likeResponses);

        response.setProfileOwnerId(post.getProfileOwner() != null ? post.getProfileOwner().getId() : null);
        response.setProfileOwnerFullName(post.getProfileOwner() != null ? post.getProfileOwner().getFirstName() + " " + post.getProfileOwner().getLastName() : null);

        return response;
    }
}
