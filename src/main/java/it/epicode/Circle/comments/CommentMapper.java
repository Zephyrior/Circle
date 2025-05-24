package it.epicode.Circle.comments;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.posts.Post;

import java.time.format.DateTimeFormatter;

public class CommentMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static CommentResponse toResponse(Comment comment) {
        CommentResponse response = new CommentResponse();

        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt().format(formatter));

        AppUser author = comment.getAuthor();
        response.setAuthorId(author.getId());
        response.setAuthorFullName(author.getFirstName()+" "+author.getLastName());
        Post post = comment.getPost();
        response.setPostId(post.getId());

        return response;
    }
}
