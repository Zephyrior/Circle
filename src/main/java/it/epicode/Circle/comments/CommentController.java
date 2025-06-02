package it.epicode.Circle.comments;

import it.epicode.Circle.common.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Validated
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse createComment(@RequestBody @Valid CommentRequest request) {
        return commentService.createComment(request);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(Long id) {
        commentService.deleteComment(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public CommentResponse updateComment(@PathVariable(name = "id") Long id, @RequestBody @Valid CommentRequest request) {
        return commentService.updateComment(id, request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<CommentResponse> getAllComments() {

        return commentService.getAllComments();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{postId}")
    public List<CommentResponse> getCommentsByPostId(@PathVariable(name = "postId") Long postId) {
        return commentService.getCommentsByPostId(postId);
    }
}
