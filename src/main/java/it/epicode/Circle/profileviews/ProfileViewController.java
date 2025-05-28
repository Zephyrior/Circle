package it.epicode.Circle.profileviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile-views")
@Validated
public class ProfileViewController {

    @Autowired
    private ProfileViewService profileViewService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{viewedUserId}")
    public ResponseEntity<Void> recordProfileView(@PathVariable Long viewedUserId) {
        profileViewService.recordProfileView(viewedUserId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public List<ProfileViewResponse> getProfileViews(@PathVariable Long userId) {
        return profileViewService.getProfileViews(userId);
    }
}
