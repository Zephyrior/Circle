package it.epicode.Circle.posts;

import it.epicode.Circle.comments.CommentMapper;
import it.epicode.Circle.comments.CommentResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static PostResponse toResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setContent(post.getContent());
        response.setMediaUrl(post.getMediaUrl());
        response.setCreatedAt(post.getCreatedAt().format(formatter));
        response.setAuthorId(post.getAuthor().getId());
        response.setAuthorFullName(post.getAuthor().getFirstName() + " " + post.getAuthor().getLastName());

        List<CommentResponse> commentResponses = post.getComments()
                .stream()
                .map(CommentMapper::toResponse)
                .collect(Collectors.toList());

        response.setComments(commentResponses);

        return response;
    }
}
