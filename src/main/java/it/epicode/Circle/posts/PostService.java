package it.epicode.Circle.posts;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.auth.AppUserRepository;
import it.epicode.Circle.circles.Circle;
import it.epicode.Circle.circles.CircleRepository;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private CircleRepository circleRepository;

    public AppUser getUserByEmail(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            throw new AccessDeniedException("Unauthorized");
        }

        return (AppUser) authentication.getPrincipal();
    }

    public CommonResponse createPost(PostRequest request) {

        AppUser author = getUserByEmail();

        Post post = new Post();
        post.setAuthor(author);
        post.setContent(request.getContent());
        post.setMediaUrl(request.getMediaUrl());

//        BeanUtils.copyProperties(request, post);
//        post.setContent(request.getContent());
//        post.setMediaUrl(request.getMediaUrl());
//
//        AppUser appUser = getUserByEmail();
//        post.setAuthor(appUser);

        if(request.getProfileOwnerId() != null){
            AppUser profileOwner = appUserRepository.findById(request.getProfileOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("Profile owner not found"));
            post.setProfileOwner(profileOwner);
        }


        Post savedPost = postRepository.save(post);

        return CommonResponse.withId(savedPost.getId());
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

    public List<Long> getFriendsIds(){
        Long currentUserId = getUserByEmail().getId();
        List<Circle> acceptedCircles = circleRepository.findAcceptedCirclesByUserId(currentUserId);

        return acceptedCircles.stream()
                .map(circle -> circle.getRequester().getId().equals(currentUserId)
                        ? circle.getReceiver().getId()
                        : circle.getRequester().getId())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Page<PostResponse> getAllPosts(int page, int size, String createdAt) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, createdAt));
        //Page<Post> postsPage = postRepository.findAll(pageable);

        Long currentUserId = getUserByEmail().getId();

        List<Long> friendsIds = new ArrayList<>(getFriendsIds());

        friendsIds.add(currentUserId);

        Page<Post> postsPage = postRepository.findByAuthorIdIn(friendsIds, pageable);

        return postsPage.map(post -> PostMapper.toResponse(post, currentUserId));
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Long currentUserId = getUserByEmail().getId();

        return PostMapper.toResponse(post, currentUserId);
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

        Long currentUserId = getUserByEmail().getId();

        return PostMapper.toResponse(updatedPost, currentUserId);
    }
}
