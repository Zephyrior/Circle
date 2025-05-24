package it.epicode.Circle.likes;

import it.epicode.Circle.common.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{postId}")
    public ResponseEntity<CommonResponse> toggleLike(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.toggleLike(postId));
    }
}
