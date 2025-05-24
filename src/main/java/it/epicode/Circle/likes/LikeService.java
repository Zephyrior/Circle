package it.epicode.Circle.likes;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.common.CommonResponse;
import it.epicode.Circle.posts.Post;
import it.epicode.Circle.posts.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    public AppUser getUserByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (AppUser) authentication.getPrincipal();
    }

    public CommonResponse toggleLike(Long postId) {
        AppUser user = getUserByEmail();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return CommonResponse.withMessage("Like removed");
        } else {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            like.onLike();
            likeRepository.save(like);
            return CommonResponse.withMessage("Like added");
        }
    }
}
