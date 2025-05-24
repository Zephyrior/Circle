package it.epicode.Circle.comments;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.common.CommonResponse;
import it.epicode.Circle.posts.Post;
import it.epicode.Circle.posts.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public AppUser getUserByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (AppUser) authentication.getPrincipal();
    }

    public CommonResponse createComment(CommentRequest request) {
        AppUser appUser = getUserByEmail();

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(appUser);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        return new CommonResponse(savedComment.getId());
    }

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        AppUser appUser = getUserByEmail();

        if (!comment.getAuthor().getId().equals(appUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    public CommentResponse updateComment(Long id, CommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        AppUser appUser = getUserByEmail();

        if (!comment.getAuthor().getId().equals(appUser.getId())) {
            throw new RuntimeException("You are not authorized to update this comment");
        }

        comment.setContent(request.getContent());

        Comment updatedComment = commentRepository.save(comment);

        return CommentMapper.toResponse(updatedComment);
    }

    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentRepository.findAll();

        return comments.stream()
                .map(CommentMapper::toResponse)
                .toList();
    }

}
