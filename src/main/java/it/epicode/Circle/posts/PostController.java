package it.epicode.Circle.posts;

import it.epicode.Circle.common.CommonResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@Validated
public class PostController {

    @Autowired
    private PostService postService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse createPost(@RequestBody @Valid PostRequest request) {
        return postService.createPost(request);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public Page<PostResponse> getAllPublicPosts(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "100") int size,
                                          @RequestParam(defaultValue = "createdAt") String createdAt) {
        return postService.getAllPosts(page, size, createdAt);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable(name = "id" ) Long id) {
        return postService.getPostById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse  updatePost(@PathVariable(name = "id") Long id, @RequestBody @Valid PostRequest request) {
        return postService.updatePost(id, request);
    }
}
