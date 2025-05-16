package it.epicode.Circle.posts;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.comments.Comment;
import it.epicode.Circle.common.CommonResponse;
import it.epicode.Circle.likes.Like;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public AppUser getUserByEmail(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AppUser) authentication.getPrincipal();
    }

    public CommonResponse  createPost(PostRequest request) {
        Post post = new Post();

        BeanUtils.copyProperties(request, post);

        AppUser appUser = getUserByEmail();
        post.setAuthor(appUser);


        Post savedPost = postRepository.save(post);

        return new CommonResponse(savedPost.getId());
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        AppUser appUser = getUserByEmail();

        if (!post.getAuthor().getId().equals(appUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this post");
        }

        postRepository.delete(post);
    }

    public Page<PostResponse> getAllPosts(int page, int size, String createdAt) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, createdAt));
        Page<Post> postsPage = postRepository.findAll(pageable);

        return postsPage.map(post -> new PostResponse(
            post.getId(),
            post.getContent(),
            post.getMediaUrl(),
            post.getCreatedAt(),
            post.getAuthor().getId(),
            post.getComments().stream().map(Comment::getId).toList(),
            post.getLikes().stream().map(Like::getId).toList()
        ));
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getMediaUrl(),
                post.getCreatedAt(),
                post.getAuthor().getId(),
                post.getComments().stream().map(Comment::getId).toList(),
                post.getLikes().stream().map(Like::getId).toList()
        );
    }

    public PostResponse updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        AppUser appUser = getUserByEmail();

        if (!post.getAuthor().getId().equals(appUser.getId())) {
            throw new RuntimeException("You are not authorized to update this post");
        }

        post.setContent(request.getContent());
        post.setMediaUrl(request.getMediaUrl());

        Post updatedPost = postRepository.save(post);

        return new PostResponse(
                updatedPost.getId(),
                updatedPost.getContent(),
                updatedPost.getMediaUrl(),
                updatedPost.getCreatedAt(),
                updatedPost.getAuthor().getId(),
                updatedPost.getComments().stream().map(Comment::getId).toList(),
                updatedPost.getLikes().stream().map(Like::getId).toList()
        );
    }
}
