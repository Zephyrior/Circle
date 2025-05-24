package it.epicode.Circle.profileviews;

import it.epicode.Circle.auth.AppUser;
import it.epicode.Circle.auth.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileViewService {

    @Autowired
    private ProfileViewRepository profileViewRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser getUserByEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AppUser) authentication.getPrincipal();
    }

    public void recordProfileView(Long viewedUserId) {

        AppUser viewer = getUserByEmail();

        AppUser viewedUser = appUserRepository.findById(viewedUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + viewedUserId));

        boolean alreadyViewed = profileViewRepository.existsByViewerAndViewed(viewer, viewedUser);

        if(alreadyViewed) {
            throw new IllegalArgumentException("Profile already viewed");
        }

        if(viewer.getId().equals(viewedUser.getId())) {
            throw new IllegalArgumentException("Cannot view own profile");
        }

        ProfileView profileView = new ProfileView();
        profileView.setViewer(viewer);
        profileView.setViewed(viewedUser);
        profileViewRepository.save(profileView);
    }

    public List<ProfileViewResponse> getProfileViews(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        List<ProfileView> profileViews = profileViewRepository.findByViewed(user);
        return profileViews.stream()
                .map(ProfileViewMapper::toProfileViewResponse)
                .toList();
    }
}
